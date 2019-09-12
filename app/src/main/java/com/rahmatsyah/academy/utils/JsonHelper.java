package com.rahmatsyah.academy.utils;

import android.app.Application;

import com.rahmatsyah.academy.data.source.remote.response.ContentResponse;
import com.rahmatsyah.academy.data.source.remote.response.CourseResponse;
import com.rahmatsyah.academy.data.source.remote.response.ModuleResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class JsonHelper {

    private Application application;

    public JsonHelper(Application application) {
        this.application = application;
    }

    private String parsingFileToString(String fileName){
        try {
            InputStream inputStream = application.getAssets().open(fileName);
            byte[] buffer = new byte[inputStream.available()];
            inputStream.read(buffer);
            inputStream.close();
            return new String(buffer);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<CourseResponse> loadCourses(){
        ArrayList<CourseResponse> list = new ArrayList<>();

        try {
            JSONObject responseObject = new JSONObject(parsingFileToString("CourseResponses.json"));
            JSONArray listArray = responseObject.getJSONArray("courses");
            for (int i = 0; i <listArray.length() ; i++) {
                JSONObject course = listArray.getJSONObject(i);

                CourseResponse courseResponse = new CourseResponse(
                        course.getString("id"),
                        course.getString("title"),
                        course.getString("description"),
                        course.getString("date"),
                        course.getString("imagePath")
                );
                list.add(courseResponse);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<ModuleResponse> loadModule(String courseId){
        String fileName = String.format("Module_%s.json", courseId);
        ArrayList<ModuleResponse> list = new ArrayList<>();

        try {
            String result = parsingFileToString(fileName);
            if (result!=null){
                JSONObject responseObject = new JSONObject(result);
                JSONArray listArray = responseObject.getJSONArray("modules");
                for (int i = 0; i < listArray.length() ; i++) {
                    JSONObject course = listArray.getJSONObject(i);
                    ModuleResponse moduleResponse = new ModuleResponse(
                            course.getString("moduleId"),
                            courseId,
                            course.getString("title"),
                            Integer.parseInt(course.getString("position"))
                    );
                    list.add(moduleResponse);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }
    public ContentResponse loadContent(String moduleId) {
        String fileName = String.format("Content_%s.json", moduleId);
        ContentResponse contentResponse = null;
        try {

            String result = parsingFileToString(fileName);
            if (result != null) {
                JSONObject responseObject = new JSONObject(result);

                String content = responseObject.getString("content");

                contentResponse = new ContentResponse(moduleId, content);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return contentResponse;
    }
}
