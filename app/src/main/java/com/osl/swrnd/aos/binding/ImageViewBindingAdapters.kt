@file:Suppress("unused")

package com.osl.swrnd.aos.binding

import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.regions.Region
import com.amazonaws.regions.Regions
import com.amazonaws.services.s3.AmazonS3Client
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.osl.swrnd.aos.binding.helper.GlideUrlExcludeQueryCacheKey
import com.osl.swrnd.aos.BuildConfig.*
import java.net.URL
import java.util.*


private var s3Client: AmazonS3Client? = null

@Suppress("IMPLICIT_CAST_TO_ANY")
@BindingAdapter(value = ["url", "glide", "holder", "awsUrl"], requireAll = false)
fun setImageUrl(view: ImageView, url: String?, glide: RequestManager?, holder: Drawable?, awsUrl: String?) {
  (glide ?: return).clear(view)

//  val t = System.currentTimeMillis()
//  var src: String? = null
  val src = when {
    awsUrl.isNullOrBlank() -> url
    awsUrl.startsWith("http") -> GlideUrlExcludeQueryCacheKey(getAwsUrl(awsUrl)/*.also { src = it }*/)
    awsUrl.contains("[.\\-]".toRegex()) -> awsUrl
    awsUrl.contains("/") -> url
    else -> view.resources.getIdentifier(awsUrl, "drawable", view.context.packageName)
  }

  val req = glide.load(src)
    .override(view.measuredWidth, view.measuredHeight)

  holder?.let {
    req.error(it)
  }

  req
    .diskCacheStrategy(DiskCacheStrategy.ALL)
    .transition(DrawableTransitionOptions.withCrossFade())
//    .listener(object : RequestListener<Drawable> {
//      override fun onResourceReady(
//        resource: Drawable?,
//        model: Any?,
//        target: Target<Drawable>?,
//        dataSource: DataSource?,
//        isFirstResource: Boolean
//      ): Boolean {
//        d(isFirstResource, System.currentTimeMillis() - t, dataSource, src)
//        return false
//      }
//
//      override fun onLoadFailed(
//        e: GlideException?,
//        model: Any?,
//        target: Target<Drawable>?,
//        isFirstResource: Boolean
//      ) = false
//
//    })
    .into(view)
}

fun getAwsUrl(awsUrl: String) = getAwsUrl(
  initAwsSetting(AWS_ACCESS_KEY, AWS_SECRET_KEY),
  URL(awsUrl).path.substring(1)
)?.toString()

@Suppress("SameParameterValue")
private fun initAwsSetting(accessKey: String?, secretKey: String?) = s3Client ?: AmazonS3Client(
  BasicAWSCredentials(accessKey ?: "", secretKey ?: ""),
  Region.getRegion(Regions.AP_NORTHEAST_2)
).also {
  s3Client = it
}

private fun getAwsUrl(s3Client: AmazonS3Client, key: String?): URL? {
  return s3Client.generatePresignedUrl(
    AWS_BUCKET_NAME,
    key,
    GregorianCalendar.getInstance().apply {
      set(get(Calendar.YEAR), get(Calendar.MONTH), get(Calendar.DATE) + 2)
      clear(Calendar.HOUR_OF_DAY)
      clear(Calendar.MINUTE)
      clear(Calendar.SECOND)
      clear(Calendar.MILLISECOND)
    }.time
  )
}
