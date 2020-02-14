package barisic.newsgetter.viewmodel.helper_classes;
import android.content.Context;
import androidx.appcompat.app.AlertDialog;
import barisic.newsgetter.R;

public class AlertBuilder {

    public static AlertDialog newDialog(Context context, int message){
        return new AlertDialog.Builder(context).setIcon(R.drawable.newsgetter_logo).setTitle(R.string.app_name).setMessage(message).setPositiveButton(R.string.dismiss, null).show();
    }
}
