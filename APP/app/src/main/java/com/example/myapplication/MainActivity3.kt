package com.example.myapplication

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit


class MainActivity3 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main3)
        //set loading screen


        //get file
        val intent = intent
        val imageUri = Uri.parse(intent.getStringExtra("imageUri"))
        val color = intent.getStringExtra("color")


        val uriPathHelper = URIPathHelper()
        val filePath = uriPathHelper.getPath(this, imageUri!!)
        val file = File(filePath)

        //change filename to black/white to understand who is playing

        val requestFile: RequestBody =
            RequestBody.create(MediaType.parse("multipart/form-data"), file)
        val multiPartBody = MultipartBody.Part.createFormData("file", file.name, requestFile)


        val okHttpClient = OkHttpClient.Builder()
            .connectTimeout(1, TimeUnit.MINUTES)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .build()

        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("http://192.168.1.9:8080/")
            .client(okHttpClient)
            .build()


        val service = retrofit.create(UploadFile::class.java)
        val call = service.uploadImage(multiPartBody)

        call.enqueue(object : Callback<FENResponse> {
            override fun onResponse(call: Call<FENResponse>, response: Response<FENResponse>) {
                if (response.code() == 200) {
                    val text = findViewById<TextView>(R.id.textView)
                    text.text = response.body().fenCode.toString()
                    val data = Intent()
                    data.putExtra("FENCode", response.body().fenCode)
                    setResult(Activity.RESULT_OK, data)
                    this@MainActivity3.finish()

                }
            }
            override fun onFailure(call: Call<FENResponse>, t: Throwable) {
                val text = findViewById<TextView>(R.id.textView)
                text.text = t.message.toString()
            }
        })

    }




}