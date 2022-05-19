package es.uca.myapplication;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

public class widget extends AppWidgetProvider{

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            //crear un intent que apunte a la actividad principal
            Intent intent = new Intent(context, MainActivity.class);
            //permite que el escritorio use nuestra app
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

            //muestra la vista en otros procesos
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget);
            views.setOnClickPendingIntent(R.id.widgetIcon, pendingIntent);

            //incluye el widget en el dispositivo
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }
}
