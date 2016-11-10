package cn.zyh.rxjavademot.base;

import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;

import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.zyh.rxjavademot.R;
import rx.Subscription;

/**
 * 基本
 */
public abstract class BaseFragment extends Fragment {

    protected Subscription subscription;

    /**
     * 关于
     */
    @OnClick(R.id.tipBt)
    public void about() {
        new AlertDialog.Builder(getActivity())
                .setTitle(getTitleRes())
                .setView(getActivity().getLayoutInflater().inflate(getDialogRes(),null))
                .show();
    }

    /**
     * 获取title
     * @return
     */
    public abstract int getTitleRes();

    /**
     * 获取dialog内容
     * @return
     */
    public abstract int getDialogRes();


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    /**
     * 取消订阅
     * unsubscribe(): 这是 Subscriber 所实现的另一个接口 Subscription 的方法，用于取消订阅。
     * 在这个方法被调用后，Subscriber 将不再接收事件。一般在这个方法调用前，可以使用 isUnsubscribed() 先判断一下状态。
     * unsubscribe() 这个方法很重要，因为在 subscribe() 之后， Observable 会持有 Subscriber 的引用，
     * 这个引用如果不能及时被释放，将有内存泄露的风险。所以最好保持一个原则：要在不再使用的时候尽快在合适的地方
     * （例如 onPause() onStop() 等方法中）调用 unsubscribe() 来解除引用关系，以避免内存泄露的发生。
     */
    public void unsubscribe() {
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
    }

}
