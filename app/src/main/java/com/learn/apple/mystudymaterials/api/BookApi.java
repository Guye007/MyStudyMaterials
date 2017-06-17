/**
 * Copyright 2016 JustWayward Team
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.learn.apple.mystudymaterials.api;

import com.learn.apple.mystudymaterials.api.support.BookApiService;
import com.learn.apple.mystudymaterials.base.Constant;
import com.learn.apple.mystudymaterials.bean.BookMixAToc;
import com.learn.apple.mystudymaterials.bean.ChapterRead;
import com.learn.apple.mystudymaterials.bean.Recommend;
import com.learn.apple.mystudymaterials.bean.user.Login;
import com.learn.apple.mystudymaterials.bean.user.LoginReq;

import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Query;
import rx.Observable;

/**
 * https://github.com/JustWayward/BookReader
 *
 * @author yuyh.
 * @date 2016/8/3.
 */
public class BookApi {

    public static BookApi instance;

    private BookApiService service;

    public BookApi(OkHttpClient okHttpClient) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constant.API_BASE_URL)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create()) // 添加Rx适配器
                .addConverterFactory(GsonConverterFactory.create()) // 添加Gson转换器
                .client(okHttpClient)
                .build();
        service = retrofit.create(BookApiService.class);
    }

    public static BookApi getInstance(OkHttpClient okHttpClient) {
        if (instance == null)
            instance = new BookApi(okHttpClient);
        return instance;
    }

    public Observable<Recommend> getRecommend(String gender) {
        return service.getRecommend(gender);
    }

    public Observable<BookMixAToc> getBookMixAToc(String bookId, String view){
        return service.getBookMixAToc(bookId,view);
    }

    public Observable<ChapterRead> getChapterRead(String url){
        return service.getChapterRead(url);
    }

    public Observable<Login> login(String platform_uid, String platform_token, String platform_code){
        LoginReq loginReq = new LoginReq();
        loginReq.platform_code = platform_code;
        loginReq.platform_token = platform_token;
        loginReq.platform_uid = platform_uid;

        return service.login(loginReq);
    }

}














