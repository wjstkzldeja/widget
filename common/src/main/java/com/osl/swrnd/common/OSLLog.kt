package com.osl.swrnd.common

import java.io.PrintWriter
import java.io.StringWriter
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Method
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.CopyOnWriteArraySet
import java.util.regex.Pattern
import kotlin.math.min

object OSLLog {
  const val V = 0
  const val D = 1
  const val I = 2
  const val W = 3
  const val E = 4
  @Suppress("MemberVisibilityCanBePrivate")
  val SYSTEM = SystemOutPrinter()
  @Suppress("MemberVisibilityCanBePrivate")
  val ANDROID = AndroidPrinter()
  private const val MAX_LOG_LINE_LENGTH = 4000
  private val mTags = ConcurrentHashMap<String, String>()
  private val ANONYMOUS_CLASS = Pattern.compile("\\$\\d+$")
  private const val STACK_DEPTH = 4
  private var mUseTags = arrayOf("tag", "TAG")
  internal var mUseFormat = false
  private var mMinLevel = V
  private val mPrinters = CopyOnWriteArraySet<Printer>()

  init {
    if (ANDROID.mLoaded) {
      usePrinter(ANDROID, true)
    } else {
      usePrinter(SYSTEM, true)
    }
  }

  @Synchronized
  fun useTags(tags: Array<String>): OSLLog? {
    mUseTags = tags
    return null
  }

  @Synchronized
  fun level(minLevel: Int): OSLLog? {
    mMinLevel = minLevel
    return null
  }

  @Synchronized
  fun useFormat(yes: Boolean): OSLLog? {
    mUseFormat = yes
    return null
  }

  @Synchronized
  fun usePrinter(p: Printer, on: Boolean = true): OSLLog? {
    if (on) {
      mPrinters.add(p)
    } else {
      mPrinters.remove(p)
    }
    return null
  }

  internal fun oslLog(level: Int, fmt: Boolean, msg: Any?, vararg args: Any?) {
    if (level < mMinLevel) {
      return
    }
    val tag = tag()
    if (mUseTags.isNotEmpty() && tag == msg) {
      if (args.size > 1) {
        print(level, tag, format(fmt, args[0], *args.copyOfRange(1, args.size)))
      } else {
        print(level, tag, format(fmt, if (args.isNotEmpty()) args[0] else ""))
      }
    } else {
      print(level, tag, format(fmt, msg, *args))
    }
  }

  private fun format(fmt: Boolean, msg: Any?, vararg args: Any?): String {
    var argsTmp = args
    var t: Throwable? = null
    if (argsTmp.lastOrNull() is Throwable) {
      t = argsTmp[argsTmp.size - 1] as Throwable
      argsTmp = argsTmp.copyOfRange(0, argsTmp.size - 1)
    }
    argsTmp = argsTmp.map {
      when (it) {
        is Function0<*> -> try {
          it.invoke()
        } catch (e: Exception) {
          e
        }
        else -> it?.toString()
      }
    }.toTypedArray()
    val tmpMsg = when (msg) {
      is Function0<*> -> try {
        msg.invoke()
      } catch (e: Exception) {
        e
      }
      else -> msg
    }
    if (fmt) {
      (tmpMsg as? String)?.let { head ->
        if (head.indexOf('%') != -1) {
          return String.format(head, *argsTmp)
        }
      }
    }
    val sb = StringBuilder()
    sb.append(tmpMsg?.toString() ?: "null")
    for (arg in argsTmp) {
      sb.append("\t")
      sb.append(arg?.toString() ?: "null")
    }
    if (t != null) {
      sb.append("\n")
      val sw = StringWriter()
      val pw = PrintWriter(sw)
      t.printStackTrace(pw)
      sb.append(sw.toString())
    }
    return sb.toString()
  }

  private fun print(level: Int, tag: String, msg: String) {
    for (line in msg.split("\\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()) {
      var line2 = line
      do {
        var splitPos = min(MAX_LOG_LINE_LENGTH, line2.length)
        var i = splitPos - 1
        while (line2.length > MAX_LOG_LINE_LENGTH && i >= 0) {
          if (" \t,.;:?!{}()[]/\\".indexOf(line2[i]) != -1) {
            splitPos = i
            break
          }
          i--
        }
        splitPos = Math.min(splitPos + 1, line2.length)
        val part = line2.substring(0, splitPos)
        line2 = line2.substring(splitPos)

        for (p in mPrinters) {
          p.print(level, tag, part)
        }
      } while (line2.isNotEmpty())
    }
  }

  private fun tag(): String {
    val stackTrace = Throwable().stackTrace
    if (stackTrace.size < STACK_DEPTH) {
      throw IllegalStateException(
        "Synthetic stacktrace didn't have enough elements: are you using proguard?"
      )
    }
    val stackTraceElement = stackTrace[STACK_DEPTH - 1]
    var className = stackTraceElement.className
    var postfix = ""
    if (mMinLevel < I) {
      postfix = "(" + stackTraceElement.fileName + ":" + stackTraceElement.lineNumber + ")"
    }
    val tag = mTags[className]
    if (tag != null) {
      return tag + postfix
    }

    try {
      val c = Class.forName(className)
      for (f in mUseTags) {
        try {
          val field = c.getDeclaredField(f)
          @Suppress("SENSELESS_COMPARISON")
          if (field != null) {
            field.isAccessible = true
            val value = field.get(null)
            if (value is String) {
              mTags[className] = value
              return value + postfix
            }
          }
        } catch (e: NoSuchFieldException) {
          // Ignore
        } catch (e: IllegalAccessException) {
        } catch (e: IllegalStateException) {
        } catch (e: NullPointerException) {
        }

      }
    } catch (e: ClassNotFoundException) {
      /* Ignore */
    }

    if (mMinLevel < I) {
      return stackTraceElement.methodName + postfix
    }

    // Check class field useTag, if exists - return it, otherwise - return the generated tag
    val m = ANONYMOUS_CLASS.matcher(className)
    if (m.find()) {
      className = m.replaceAll("")
    }
    return className.substring(className.lastIndexOf('.') + 1) + postfix
  }

  interface Printer {
    fun print(level: Int, tag: String, msg: String)
  }

  class SystemOutPrinter : Printer {

    override fun print(level: Int, tag: String, msg: String) {
      println(LEVELS[level] + "/" + tag + ": " + msg)
    }

    companion object {
      private val LEVELS = arrayOf("V", "D", "I", "W", "E")
    }
  }

  class AndroidPrinter : Printer {

    private val mLogClass: Class<*>?
    private val mLogMethods: Array<Method?>
    val mLoaded: Boolean

    init {
      var oslLogClass: Class<*>? = null
      var loaded = false
      mLogMethods = arrayOfNulls(METHOD_NAMES.size)
      try {
        oslLogClass = Class.forName("android.util.Log")
        for (i in METHOD_NAMES.indices) {

          mLogMethods[i] = oslLogClass.getMethod(METHOD_NAMES[i], String::class.java, String::class.java)
        }
        loaded = true
      } catch (e: NoSuchMethodException) {
        // Ignore
      } catch (e: ClassNotFoundException) {
      }

      mLogClass = oslLogClass
      mLoaded = loaded
    }

    override fun print(level: Int, tag: String, msg: String) {
      try {
        if (mLoaded) {
          mLogMethods[level]?.invoke(null, tag, msg)
        }
      } catch (e: InvocationTargetException) {
        // Ignore
      } catch (e: IllegalAccessException) {
      }

    }

    companion object {

      private val METHOD_NAMES = arrayOf("v", "d", "i", "w", "e")
    }
  }
}

@Synchronized
fun v(msg: Any?, vararg args: Any?): OSLLog? {
  OSLLog.oslLog(OSLLog.V, OSLLog.mUseFormat, msg, *args)
  return null
}

@Synchronized
fun d(msg: Any?, vararg args: Any?): OSLLog? {
  OSLLog.oslLog(OSLLog.D, OSLLog.mUseFormat, msg, *args)
  return null
}

@Synchronized
fun d(msg: Function0<*>) {
  OSLLog.oslLog(OSLLog.D, OSLLog.mUseFormat, msg)
}

@Synchronized
fun i(msg: Any?, vararg args: Any?): OSLLog? {
  OSLLog.oslLog(OSLLog.I, OSLLog.mUseFormat, msg, *args)
  return null
}

@Synchronized
fun w(msg: Any?, vararg args: Any?): OSLLog? {
  OSLLog.oslLog(OSLLog.W, OSLLog.mUseFormat, msg, *args)
  return null
}

@Synchronized
fun e(msg: Any?, vararg args: Any?): OSLLog? {
  OSLLog.oslLog(OSLLog.E, OSLLog.mUseFormat, msg, *args)
  return null
}

@Synchronized
fun v(): OSLLog? {
  OSLLog.oslLog(OSLLog.V, OSLLog.mUseFormat, null)
  return null
}

@Synchronized
fun d(): OSLLog? {
  OSLLog.oslLog(OSLLog.D, OSLLog.mUseFormat, null)
  return null
}

@Synchronized
fun i(): OSLLog? {
  OSLLog.oslLog(OSLLog.I, OSLLog.mUseFormat, null)
  return null
}

@Synchronized
fun w(): OSLLog? {
  OSLLog.oslLog(OSLLog.W, OSLLog.mUseFormat, null)
  return null
}

@Synchronized
fun e(): OSLLog? {
  OSLLog.oslLog(OSLLog.E, OSLLog.mUseFormat, null)
  return null
}