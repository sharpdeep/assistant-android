package com.sharpdeep.assistant_android.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.sharpdeep.assistant_android.R;
import com.sharpdeep.assistant_android.util.L;
import com.victor.loading.book.BookLoading;

import me.drakeet.materialdialog.MaterialDialog;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by bear on 16-4-1.
 */
public class LoadingDialog {
    private Context mContext;
    private MaterialDialog mDialog;
    private View mDialogContentView;
    private BookLoading mLoadingView;
    private ErrorHandler mErrorHandler;

    public LoadingDialog(Context context){
        this.mContext = context;
        mDialogContentView = LayoutInflater.from(this.mContext).inflate(R.layout.item_loading_dialog,null);
        mLoadingView = (BookLoading) mDialogContentView.findViewById(R.id.loading);
    }


    public void show(String title){
        mDialog = new MaterialDialog(this.mContext)
                .setTitle(title)
                .setContentView(mDialogContentView)
                .setCanceledOnTouchOutside(false);

        mDialog.show();
        if (!mLoadingView.isStart()){
            mLoadingView.start();
        }
        L.d("show");
    }

    public LoadingDialog setDismissObservable(Observable<Boolean> observable){
        observable.observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Boolean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        dismiss();
                        if (mErrorHandler != null){
                            mErrorHandler.handler(e);
                        }
                    }

                    @Override
                    public void onNext(Boolean finish) {
                        if (finish) {
                            L.d("dismiss");
                            dismiss();
                        }
                    }
                });
        return this;
    }

    public LoadingDialog setErrorHandler(ErrorHandler handler){
        this.mErrorHandler = handler;
        return this;
    }

    private void dismiss(){
        if (mLoadingView.isStart()){
            mLoadingView.stop();
            mDialog.dismiss();
        }
    }

    public interface ErrorHandler{
        void handler(Throwable e);
    }
}
