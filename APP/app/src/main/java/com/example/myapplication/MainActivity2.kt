package com.example.myapplication

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Button
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.PermissionChecker
import androidx.core.content.PermissionChecker.checkSelfPermission
import androidx.fragment.app.Fragment


class MainActivity2 : AppCompatActivity() {
    private var imageUri: Uri? = null
    private var imageView: ImageView? = null
    private var button: Button? = null
    private var white: Button? = null
    private var black: Button? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        imageView = findViewById<ImageView>(R.id.imageview_picture)
        button = findViewById<Button>(R.id.button_take_picture)
        white = findViewById<Button>(R.id.White)
        black = findViewById<Button>(R.id.Black)
        findViewById<Button>(R.id.button_take_picture).setOnClickListener {
            // Request permission
            val permissionGranted = requestCameraPermission()
            if (permissionGranted) {
                // Open the camera interface
                openCameraInterface()
            }

        }

    }


    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            // Permission was granted
            openCameraInterface()
        } else {
            // Permission was denied
            showAlert("Camera permission was denied. Unable to take a picture.")
        }
    }

    private fun requestCameraPermission(): Boolean {
        var permissionGranted = false

        // If system os is Marshmallow or Above, we need to request runtime permission
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val cameraPermissionNotGranted = checkSelfPermission(this as Context,
                Manifest.permission.CAMERA
            ) == PermissionChecker.PERMISSION_DENIED
            if (cameraPermissionNotGranted) {
                // Display permission dialog
                requestPermissionLauncher.launch(Manifest.permission.CAMERA)
            } else {
                // Permission already granted
                permissionGranted = true
            }
        } else {
            // Android version earlier than M -&gt; no need to request permission
            permissionGranted = true
        }

        return permissionGranted
    }


    private fun showAlert(message: String) {
        val builder = AlertDialog.Builder(this as Context)
        builder.setMessage(message)
        builder.setPositiveButton("OK", null)

        val dialog = builder.create()
        dialog.show()
    }
    //private val IMAGE_CAPTURE_CODE = 1001

    private fun openCameraInterface() {
        val values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE, "take_picture.jpg")
        values.put(MediaStore.Images.Media.DESCRIPTION, "take_picture_description")
        imageUri =
            this.contentResolver?.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)

        // Create camera intent
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)

        // Launch intent
        result.launch(intent)
    }

    private var result =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                // Set image captured to image view
                imageView?.setImageURI(imageUri)
                white?.visibility = View.VISIBLE
                black?.visibility = View.VISIBLE
                white?.setOnClickListener {
                    // Request permission
                    val i = Intent(this@MainActivity2, MainActivity3::class.java)
                    i.putExtra("color", "white")
                    i.putExtra("imageUri", imageUri.toString())
                    result2.launch(i)
                }
                black?.setOnClickListener {
                    // Request permission

                    val i = Intent(this@MainActivity2, MainActivity3::class.java)
                    i.putExtra("color", "black")
                    i.putExtra("imageUri", imageUri.toString())
                    result2.launch(i)
                }
            } else {
                // Failed to take picture
                showAlert("Failed to take camera picture")
            }
        }

    private var result2 =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                // Set image captured to image view
                val data = Intent()
                data.putExtra("FENCode", result.data?.getStringExtra("FENCode"))
                setResult(Activity.RESULT_OK, data)
                this@MainActivity2.finish()

            }
            else if(result.resultCode == 8) {
                // Failed to take picture
                showAlert("Bad picture, could not be processed")
            }
            else {
                // Failed to take picture
                showAlert("Failed to make connection with server")
            }
        }
}