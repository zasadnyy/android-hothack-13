/*
 * Copyright 2012 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ua.org.gdg.devfest.iosched.appwidget;

import ua.org.gdg.devfest.iosched.R;
import ua.org.gdg.devfest.iosched.sync.SyncHelper;
import ua.org.gdg.devfest.iosched.ui.HomeActivity;
import ua.org.gdg.devfest.iosched.ui.TaskStackBuilderProxyActivity;

import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.widget.RemoteViews;

import static ua.org.gdg.devfest.iosched.util.LogUtils.makeLogTag;

/**
 * The app widget's AppWidgetProvider.
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class ScheduleWidgetProvider extends AppWidgetProvider {
    private static final String TAG = makeLogTag(ScheduleWidgetProvider.class);

    private static final String REFRESH_ACTION =
            "ua.org.gdg.devfest.iosched.appwidget.action.REFRESH";
    private static final String EXTRA_PERFORM_SYNC =
            "ua.org.gdg.devfest.iosched.appwidget.extra.PERFORM_SYNC";

    public static Intent getRefreshBroadcastIntent(Context context, boolean performSync) {
        return new Intent(REFRESH_ACTION)
                .setComponent(new ComponentName(context, ScheduleWidgetProvider.class))
                .putExtra(EXTRA_PERFORM_SYNC, performSync);
    }

    @Override
    public void onReceive(final Context context, Intent widgetIntent) {
        final String action = widgetIntent.getAction();

        if (REFRESH_ACTION.equals(action)) {
            final boolean shouldSync = widgetIntent.getBooleanExtra(EXTRA_PERFORM_SYNC, false);

            // Trigger sync
            if (shouldSync) {
                SyncHelper.requestManualSync(context);
            }

            // Notify the widget that the list view needs to be updated.
            final AppWidgetManager mgr = AppWidgetManager.getInstance(context);
            final ComponentName cn = new ComponentName(context, ScheduleWidgetProvider.class);
            mgr.notifyAppWidgetViewDataChanged(mgr.getAppWidgetIds(cn),
                    R.id.widget_schedule_list);
        }
        super.onReceive(context, widgetIntent);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            // Specify the service to provide data for the collection widget.  Note that we need to
            // embed the appWidgetId via the data otherwise it will be ignored.
            final Intent intent = new Intent(context, ScheduleWidgetRemoteViewsService.class)
                    .putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
            intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
            final RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.widget);
            rv.setRemoteAdapter(appWidgetId, R.id.widget_schedule_list, intent);

            // Set the empty view to be displayed if the collection is empty.  It must be a sibling
            // view of the collection view.
            rv.setEmptyView(R.id.widget_schedule_list, android.R.id.empty);
            rv.setTextViewText(android.R.id.empty, context.getResources().getString(R.string.empty_widget_text));

            final PendingIntent refreshPendingIntent = PendingIntent.getBroadcast(context, 0,
                    getRefreshBroadcastIntent(context, true), PendingIntent.FLAG_UPDATE_CURRENT);
            rv.setOnClickPendingIntent(R.id.widget_refresh_button, refreshPendingIntent);

            final Intent onClickIntent = TaskStackBuilderProxyActivity.getTemplate(context);
            final PendingIntent onClickPendingIntent = PendingIntent.getActivity(context, 0,
                    onClickIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            rv.setPendingIntentTemplate(R.id.widget_schedule_list, onClickPendingIntent);

            final Intent openAppIntent = new Intent(context, HomeActivity.class)
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            final PendingIntent openAppPendingIntent = PendingIntent.getActivity(context, 0,
                    openAppIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            rv.setOnClickPendingIntent(R.id.widget_logo, openAppPendingIntent);

            appWidgetManager.updateAppWidget(appWidgetId, rv);
        }
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }
}
