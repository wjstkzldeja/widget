package com.osl.swrnd.data.source.remote

import com.osl.swrnd.entity.local.req.TestReq
import com.osl.swrnd.entity.local.res.TestVo
import io.reactivex.Flowable
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query


interface ApiService {

  @GET(getTestData)
  fun getTestData(@Query("req") req: String): Flowable<TestVo>

  @POST(postTestData)
  fun getTestData2(@Body testReq: TestReq): Flowable<TestVo>?
}
