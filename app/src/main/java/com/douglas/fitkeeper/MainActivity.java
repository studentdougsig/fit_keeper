package com.douglas.fitkeeper;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Toast;


import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subscriptions.Subscriptions;

public class MainActivity extends Activity implements Callback<List<Post>> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rxJavaTest();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResponse(Response<List<Post>> response, Retrofit retrofit) {
        setProgressBarIndeterminateVisibility(false);
//        ArrayAdapter<Post> adapter = (ArrayAdapter<Post>) getListAdapter();
//        adapter.clear();
//        adapter.addAll(response.body().items);
    }

    @Override
    public void onFailure(Throwable t) {
        Toast.makeText(MainActivity.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
    }

    public void rxJavaTest() {
        Observable<String> myObservable = Observable.create(
                new Observable.OnSubscribe<String>() {
                    @Override
                    public void call(Subscriber<? super String> sub) {
                        sub.onNext("Hello, world!");
                        sub.onCompleted();
                    }
                }
        );
        Subscriber<String> mySubscriber = new Subscriber<String>() {
            @Override
            public void onNext(String s) {
                System.out.println(s);
            }

            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
            }
        };
        myObservable.map(new Func1<String, String>() {
            @Override
            public String call(String s) {
                return s + " -Dan";
            }
        })
        .subscribe(mySubscriber);

        Observable<JSONObject> rawEntriesObservable = getRawEntries("example query");
        Subscription subscription = rawEntriesObservable
                .map(new Func1<JSONObject, List<String>>() {
                    public List<String> call(JSONObject rawEntries) {
                        return new ArrayList();
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(new Func1<List<String>, Observable<String>>() {
                    @Override
                    public Observable<String> call(List<String> entries) {return Observable.from(entries);
                    }
                })
                .flatMap(new Func1<String, Observable<String>>() {
            @Override
            public Observable<String> call(String entry) {
                return getTitle(entry);
            }
        })
                .filter(new Func1<String, Boolean>() {
            @Override
            public Boolean call(String title) {
                return title != null;
            }
        })
                .take(5)
                .subscribe(new Action1<String>() {
            @Override
            public void call(String entry) {System.out.println(entry);
            }
        });
        subscription.unsubscribe();
    }

    public Observable<JSONObject> getRawEntries(String query) {
        return Observable.just(new JSONObject());
    }

    public Observable<String> getTitle(String entry) {
        return Observable.just(entry + "-title");
    }


}
