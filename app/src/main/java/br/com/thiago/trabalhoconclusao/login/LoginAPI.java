package br.com.thiago.trabalhoconclusao.login;

import br.com.thiago.trabalhoconclusao.model.User;
import retrofit2.http.GET;
import rx.Observable;

public interface LoginAPI {

    @GET("/v2/58b9b1740f0000b614f09d2f")
    Observable<User> getUser();

}
