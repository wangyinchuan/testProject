package com.example.xapiservice.app;

import com.example.xapiservice.app.utils.JsonUtil;
import com.example.xapiservice.app.entity.*;

import java.util.ArrayList;
import java.util.List;

public class JsonTest {
    public static void main(String[] args) {
        Student model=new Student();
        model.setStuId(Long.valueOf("1"));
        model.setStuName("name1");
        List<String> datastr=new ArrayList<>();
        for (int i=0;i<5;i++){
            datastr.add(i+"ok");
        }
        model.setListStr(datastr);

        List<App> appList=new ArrayList<>();
        App appmodel=new App();
        appmodel.setAppKey("123456");
        appList.add(appmodel);
        model.setAppList(appList);

        String jsonstr= JsonUtil.toJSONString(model);
        System.out.print(jsonstr);

        List<Student> model1=new ArrayList<>();
        model1= JsonUtil.toList(jsonstr,Student.class);
        System.out.println(model1.size()+"\\"+model1.get(0).getListStr().size()+"\\"+model1.get(0).getStuId());

        Student modelok=new Student();
        modelok.setStuId(Long.valueOf("1"));
        modelok.setStuName("name1");
        List<String> datastrs=new ArrayList<>();
        for (int i=0;i<5;i++){
            datastrs.add(i+"ok");
        }
        modelok.setListStr(datastrs);
        model1.add(modelok);
        String jsonstrList= JsonUtil.toJSONString(model1);
        System.out.print(jsonstrList);

        List<Student> model2=new ArrayList<>();
        model2= JsonUtil.toList(jsonstrList,Student.class);
        System.out.println(model2.size()+"\\"+model2.get(1).getListStr().size()+"\\"+model2.get(0).getStuId());
        String jsonstre= JsonUtil.toJSONString(model2);
        System.out.print(jsonstre);

    }
}
