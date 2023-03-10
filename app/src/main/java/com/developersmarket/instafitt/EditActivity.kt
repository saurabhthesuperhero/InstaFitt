package com.developersmarket.instafitt

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.developersmarket.instafitt.adapter.ItemAdapter
import com.developersmarket.instafitt.databinding.ActivityEditBinding
import com.developersmarket.instafitt.model.ItemEditAction
import com.developersmarket.instafitt.stickerview.StickerView
import com.developersmarket.instafitt.utils.AdLoader
import com.developersmarket.instafitt.utils.ImageNonStaticUtils
import com.developersmarket.instafitt.utils.ImageUtils
import com.github.dhaval2404.colorpicker.ColorPickerDialog
import com.github.dhaval2404.colorpicker.listener.ColorListener
import com.github.dhaval2404.colorpicker.model.ColorShape
import com.github.dhaval2404.imagepicker.ImagePicker

class EditActivity : AppCompatActivity() {
    private var itemEditActionArrayList = arrayListOf<ItemEditAction>()
    private lateinit var binding: ActivityEditBinding

    var imageBackUrl = ""
    private lateinit var imageUri: Uri
    private val DefaultColor = intArrayOf(Color.WHITE)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditBinding.inflate(layoutInflater)
        setContentView(binding.root)
        imageUri = intent.getParcelableExtra(IMAGE_URI_EXTRA) ?: Uri.EMPTY
        if (imageUri == Uri.EMPTY) {
            imageUri = (Uri.parse(intent.getStringExtra(IMAGE_URI_EXTRA)) ?: Uri.EMPTY)
        }
        binding.ivMainImage.setImageURI(imageUri)
        AdLoader.showInterstitialAd()

        initData()
        initListener()
        initRecycler()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initListener() {
        binding.llBackground.setOnClickListener(View.OnClickListener {
            binding.llMenuAspectRatio.setVisibility(View.GONE)
            binding.llMenuBackground.setVisibility(View.VISIBLE)
        })
        binding.llRatio.setOnClickListener(View.OnClickListener {
            binding.llMenuAspectRatio.setVisibility(View.VISIBLE)
            binding.llMenuBackground.setVisibility(View.GONE)
        })
        binding.btSelectImage.setOnClickListener(View.OnClickListener {
            ImagePicker.with(this@EditActivity)
                .crop() //Crop image(Optional), Check Customization for more option
                .compress(1024) //Final image size will be less than 1 MB(Optional)
                .maxResultSize(
                    1080,
                    1080
                ) //Final image resolution will be less than 1080 x 1080(Optional)
                .start()
        })

        binding.btTransparentBack.setOnClickListener(View.OnClickListener {
            ImageUtils.canvasColor = Color.TRANSPARENT
            ImageUtils.backBitmap = null
            ImageUtils.mode = "COLOR"
            ImageUtils.resizeSquareImage(
                imageUri.toString(),
                imageBackUrl,
                binding.ivMainImage,
                this@EditActivity,
                1f
            )
        })
        binding.btOriginal.setOnClickListener(View.OnClickListener {
            binding.ivMainImage.reset()
            Glide.with(this)
                .load(imageUri)
                .into(binding.ivMainImage)
        })

        binding.llColorPicker.setOnClickListener(View.OnClickListener {
            ColorPickerDialog.Builder(this@EditActivity)
                .setTitle("Pick Color")
                .setColorShape(ColorShape.SQAURE)
                .setDefaultColor(DefaultColor[0])
                .setColorListener(object : ColorListener {
                    override fun onColorSelected(color: Int, colorHex: String) {
                        // Handle Color Selection


                        // Handle Color Selection
                        ImageUtils.canvasColor = color
                        ImageUtils.backBitmap = null
                        ImageUtils.mode = "COLOR"
                        ImageUtils.resizeSquareImage(
                            imageUri.toString(), imageBackUrl,

                            binding.ivMainImage,
                            this@EditActivity,
                            1f
                        )


                    }
                })
                .show()
        })

        binding.btTransparentBack.setOnClickListener(View.OnClickListener {
            ImageUtils.canvasColor = Color.TRANSPARENT
            ImageUtils.resizeSquareImage(
                imageUri.toString(), imageBackUrl,

                binding.ivMainImage,
                this@EditActivity,
                1f
            )
        })


        binding.llDownload.setOnClickListener(View.OnClickListener {
            binding.ivMainImage.reset()
            HideStickers()

            val imageNonStaticUtils = ImageNonStaticUtils()

            AdLoader.showRewardedInterstitialAd(object : AdLoader.RewardListener {
                override fun onRewardEarned() {
                    Toast.makeText(this@EditActivity, "Downloading", Toast.LENGTH_SHORT).show();
                    imageNonStaticUtils.saveImage(
                        this@EditActivity, binding.ivMainImage,
                        binding.frameLayout
                    )
                }

                override fun onError() {
                    Toast.makeText(this@EditActivity, "Downloading", Toast.LENGTH_SHORT).show();
                    imageNonStaticUtils.saveImage(
                        this@EditActivity, binding.ivMainImage,
                        binding.frameLayout
                    )
                }

            })
        })

        binding.llShare.setOnClickListener(View.OnClickListener {
            binding.ivMainImage.reset()
            HideStickers()

            AdLoader.showRewardedInterstitialAd(object : AdLoader.RewardListener {
                override fun onRewardEarned() {
                    ImageUtils.shareImage(
                        this@EditActivity,
                        binding.ivMainImage, binding.frameLayout
                    )
                }

                override fun onError() {
                    ImageUtils.shareImage(
                        this@EditActivity,
                        binding.ivMainImage, binding.frameLayout
                    )
                }

            })

        })

        binding.llText.setOnClickListener(View.OnClickListener {
            ImageUtils.opendialogtext(
                this@EditActivity,
                binding.frameLayout
            )
        })

        binding.ivMainImage.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                if (event?.pointerCount == 2) {
                    binding.ivMainImage.scaleDetector.onTouchEvent(event)
                }
                if (event?.pointerCount == 1 && event.action == MotionEvent.ACTION_DOWN) {
                    HideStickers();
                }
                return true;
            }


        })
    }


    private fun initRecycler() {

        val adapter =
            ItemAdapter(itemEditActionArrayList, object : ItemAdapter.OnItemClickListener {
                override fun onItemClick(item: ItemEditAction) {
                    val targetAspectRatio = when (item.name) {
                        "Square" -> 1f
                        "3:4" -> 3f / 4f
                        "3:2" -> 3f / 2f
                        "16:9" -> 16f / 9f
                        else -> 1f
                    }
                    if (targetAspectRatio.equals(1f)) {
                        ImageUtils.resizeSquareImage(
                            imageUri.toString(), imageBackUrl,

                            binding.ivMainImage,
                            this@EditActivity,
                            targetAspectRatio
                        )
                    } else {
                        ImageUtils.resizeImage(
                            imageUri.toString(),
                            imageBackUrl,
                            binding.ivMainImage,
                            this@EditActivity,
                            targetAspectRatio,
                            this@EditActivity
                        )
                    }
                }

            })

        binding.rvList.adapter = adapter
    }

    private fun initData() {
        itemEditActionArrayList.add(ItemEditAction("1:1", R.drawable.square))
        itemEditActionArrayList.add(ItemEditAction("3:4", R.drawable.square))
        itemEditActionArrayList.add(ItemEditAction("3:2", R.drawable.square))
        itemEditActionArrayList.add(ItemEditAction("16:9", R.drawable.square))
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            //Image Uri will not be null for RESULT_OK
            val uri: Uri = data?.data!!

            // Use Uri object instead of File to avoid storage permissions
            imageBackUrl = uri.toString()
            ImageUtils.mode = "IMAGE"
            ImageUtils.resizeSquareImage(
                imageUri.toString(),
                imageBackUrl,
                binding.ivMainImage,
                this@EditActivity,
                1f
            )
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show()
        }
    }

    fun HideStickers() {
        var fm = binding.frameLayout
        var childcount: Int = binding.frameLayout.childCount

        if (childcount != 0) {
            for (i in 0 until childcount) {
                var v: View = fm.getChildAt(i)

                if (v is StickerView) {
                    v.setControlItemsHidden(true)
                }
            }
        }
    }

    companion object {
        const val IMAGE_URI_EXTRA = "image_uri_extra"
    }
}