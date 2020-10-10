package jp.mkjiro.reversi.data.api

import jp.mkjiro.reversi.data.api.entity.SpreadSheet
import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Url

interface SampleAPIService{
    @GET
    fun getSpreadSheet(@Url url:String):Single<Response<List<SpreadSheet>>>
}