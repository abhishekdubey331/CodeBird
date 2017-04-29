package com.nasaspaceapps.codebird.utils;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.nasaspaceapps.codebird.pojo.User;
import com.pixplicity.easyprefs.library.Prefs;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class UserRegistration {

    private RequestQueue requestQueue;
    private Context context;
    private User user;

    private String user_registration_url = "http://52.26.68.140:8080/register";
    private String user_login_url = "http://52.26.68.140:8080/login";

    public UserRegistration(Context context, User user) {
        requestQueue = Volley.newRequestQueue(context);
        this.context = context;
        this.user = user;
    }


    public void sendAgainForUserId() {

        StringRequest strReq = new StringRequest(Request.Method.POST,
                user_login_url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                Prefs.putBoolean("login_status", true);

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("userDetails");


                    Log.e("Response", response);

                    Prefs.putString("user_id", jsonArray.getJSONObject(0).getString("user_id"));
                    Prefs.putString("token", jsonObject.getString("token"));


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("VolleyError", "Error: " + error.getMessage());
            }
        }) {

            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("email", user.getEmail());
                params.put("gid", user.getGoogle_ID());

                return params;
            }

        };

        requestQueue.add(strReq);
    }


    public void sendRequest() {

        StringRequest strReq = new StringRequest(Request.Method.POST,
                user_registration_url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {
                    sendAgainForUserId();


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("VolleyError", "Error: " + error.getMessage());
            }
        }) {

            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("username", user.getFullname());
                params.put("email", user.getEmail());
                params.put("gid", user.getGoogle_ID());
                params.put("dp", user.getPic());

                return params;
            }

        };

        requestQueue.add(strReq);
    }
}
