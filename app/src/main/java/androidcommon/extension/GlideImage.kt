package androidcommon.extension

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.annotation.DrawableRes
import com.bumptech.glide.Glide
import com.bumptech.glide.request.transition.Transition
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.CustomTarget
import com.heartsun.shivanagarkp.R

fun ImageView.load(
    url: String?,
    @DrawableRes placeholder: Int = R.drawable.loading_anim,
    @DrawableRes errorImage: Int = R.drawable.ic_person_filled
) {
    val requestOptions = RequestOptions().placeholder(placeholder).error(errorImage)

    Glide.with(context)
        .setDefaultRequestOptions(requestOptions)
        .load(url).into(this)
}

fun ImageView.loadNonCache(
    url: String?, @DrawableRes placeholder: Int = R.drawable.loading_anim,
    @DrawableRes errorImage: Int = R.drawable.error_placeholder
) {
    val requestOptions = RequestOptions()/*.placeholder(placeholder)*/.error(errorImage)
    Glide.with(context)
        .setDefaultRequestOptions(requestOptions)
        .load(url)
        .diskCacheStrategy(DiskCacheStrategy.NONE)
        .skipMemoryCache(true)
        .into(this)
}

fun Context.getCustomBitmapDrawable(
    imageUrl: String?,
    onResourceReady: (resource: Bitmap) -> Unit
) {

    Glide.with(this)
        .asBitmap()
        .load(imageUrl)
        .into(object : CustomTarget<Bitmap>() {
            override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {

                onResourceReady.invoke(resource)
            }

            override fun onLoadCleared(placeholder: Drawable?) {
                // this is called when imageView is cleared on lifecycle call or for
                // some other reason.
                // if you are referencing the bitmap somewhere else too other than this imageView
                // clear it here as you can no longer have the bitmap
            }
        })
}