package com.osl.swrnd.aos

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import com.osl.swrnd.common.d

/**
 * Implementation of App Widget functionality.
 */
class AppWidget : AppWidgetProvider() {

    private var testValue: String? = "null"

    override fun onReceive(context: Context?, intent: Intent?) {
        super.onReceive(context, intent)
        testValue = intent?.getStringExtra("test") ?: "null"
        d({ "logTest : onReceive : ${testValue}" })
        if (testValue == "null" || testValue.isNullOrEmpty()) {
            return
        }
        val appWidgetManager = AppWidgetManager.getInstance(context)
        val widgetName = ComponentName(context!!.packageName, AppWidget::class.java.name)
        val widgetIds = appWidgetManager.getAppWidgetIds((widgetName))
        this.onUpdate(context, appWidgetManager, widgetIds)
    }

    /**
     * 위젯이 바탕화면에 설치될 때마다 호출되는 함수
     * @param context
     * @param appWidgetManager
     * @param appWidgetIds
     */
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        d({ "logTest : onUpdate : ${testValue}" })
        // There may be multiple widgets active, so update all of them
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }

        if (testValue == "null" || testValue.isNullOrEmpty()) {
            return
        }
        /*text change*/
        val views = RemoteViews(context.packageName, R.layout.app_widget)
        views.setTextViewText(R.id.appwidget_text, testValue)
        appWidgetManager.updateAppWidget(appWidgetIds, views)

    }

    /**
     * 앱 위젯이 최초로 설치되는 순간 호출되는 함수
     * @param context
     */
    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created
    }

    /**
     * 위젯이 제거되는 순간 호출되는 함수
     * @param context
     */
    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    /**
     * 위젯이 마지막으로 제거되는 순간 호출되는 함수
     * @param context
     * @param appWidgetIds
     */
    override fun onDeleted(context: Context?, appWidgetIds: IntArray?) {
    }
}

/**
 * 위젯의 크기 및 옵션이 변경될 때마다 호출되는 함수
 * @param context
 * @param appWidgetManager
 * @param appWidgetId
 */
internal fun updateAppWidget(
    context: Context,
    appWidgetManager: AppWidgetManager,
    appWidgetId: Int
) {
    d({ "logTest : updateAppWidget " })
    val widgetText = context.getString(R.string.appwidget_text)
    // Construct the RemoteViews object
    val views = RemoteViews(context.packageName, R.layout.app_widget)
    views.setTextViewText(R.id.appwidget_text, widgetText)

    // Instruct the widget manager to update the widget
    appWidgetManager.updateAppWidget(appWidgetId, views)
}