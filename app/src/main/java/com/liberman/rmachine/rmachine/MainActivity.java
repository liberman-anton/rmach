package com.liberman.rmachine.rmachine;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private final int REQ_CODE = 100;
    private TextView textView;
//    String url = "http://my-json-feed";
//
//    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
//            (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
//
//                @Override
//                public void onResponse(JSONObject response) {
//                    textView.setText("Response: " + response.toString());
//                }
//            }, new Response.ErrorListener() {
//
//                @Override
//                public void onErrorResponse(VolleyError error) {
//                    // TODO: Handle error
//
//                }
//            });

    private OkHttpClient client = new OkHttpClient();
    private static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = findViewById(R.id.text);
        ImageView speak = findViewById(R.id.speak);
        speak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                        RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
                intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                        "Need to speak");
                try {
                    startActivityForResult(intent, REQ_CODE);
                } catch (ActivityNotFoundException a) {
                    Toast.makeText(getApplicationContext(),
                            "Sorry your device not supported",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    //MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
                    //String str =
                            //sendPost(result.get(0));
                    try {
                        String str = new StringBuilder(result.get(0)).reverse().toString().replaceAll("\"","");
                        String json = "{'query':{'match':{'title':{'query':'" + str + "','operator':'or'}}}}";
                        RequestBody body = RequestBody.create(JSON, json);;
                        Request request = new Request.Builder()
                                .header("Authorization", "")
                                .url(" https://gimli-eu-west-1.searchly.com")
                                .post(body)
                                .build();
                        Response response = client.newCall(request).execute();
                        textView.setText(response.toString());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                break;
            }
        }
    }

//    private String sendPost(String s) {
//        try {
//            URL url = new URL("https://www.tanelikorri.com/");
//
//            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//
//            // Log the server response code
//            int responseCode = connection.getResponseCode();
//           // Log.i(TAG, "Server responded with: " + responseCode);
//
//            // And if the code was HTTP_OK then parse the contents
//            if (responseCode == HttpURLConnection.HTTP_OK) {
//
//                // Convert request content to string
//                InputStream is = connection.getInputStream();
//                String content = convertInputStream(is, "UTF-8");
//                is.close();
//
//                return content;
//            }
//
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return "error";
//    }
//    private String convertInputStream(InputStream is, String encoding) {
//        Scanner scanner = new Scanner(is, encoding).useDelimiter("\\A");
//        return scanner.hasNext() ? scanner.next() : "";
//    }

}