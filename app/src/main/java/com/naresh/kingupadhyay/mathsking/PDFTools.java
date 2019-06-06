package com.naresh.kingupadhyay.mathsking;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.SyncStateContract;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import static android.net.Uri.*;

public class PDFTools {

    //private static final String GOOGLE_DRIVE_PDF_READER_PREFIX = "http://drive.google.com/viewer?url=";
    private static final String PDF_MIME_TYPE = "application/pdf";
    private static final String HTML_MIME_TYPE = "text/html";
    private static Basic_activity basic_activity = new Basic_activity();
    private static long downloadId1;

    /**
     * If a PDF reader is installed, download the PDF file and open it in a reader.
     * Otherwise ask the user if he/she wants to view it in the Google Drive online PDF reader.<br />
     * <br />
     * <b>BEWARE:</b> This method
     * @param context
     * @param pdfUrl
     * @return
     */
    public static void showPDFUrl(final Context context,final String title, final String pdfUrl ) {
        if ( isPDFSupported( context ) ) {
            downloadAndOpenPDF(context, title, pdfUrl);
        } else {
            askToOpenPDFThroughGoogleDrive( context,title, pdfUrl );
        }
    }


    public static boolean isDownloadManagerAvailable(Context context) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            return true;
        }
        return false;
    }
    /**
     * Downloads a PDF with the Android DownloadManager and opens it with an installed PDF reader app.
     * @param context
     * @param pdfUrl
     */
    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    public static void downloadAndOpenPDF(final Context context, final String title,final String pdfUrl) {
        try{
            // Get filename
            final String filename = title + ".pdf";
            // download link "https://drive.google.com/uc?export=download&id=1KkFEEsrfNDiG4sfDocvDIrjgLhGEWSvY"
            final String downloadLink = "https://drive.google.com/uc?export=download&id="+ pdfUrl.substring( pdfUrl.lastIndexOf( "=" ) + 1 );
            // The place where the downloaded PDF file will be put
            final File tempFile = new File( context.getExternalFilesDir( Environment.DIRECTORY_DOWNLOADS ), filename );

            if ( tempFile.isFile()) {
                // If we have downloaded the file before, just go ahead and show it.
                Toast.makeText(context, "Already Offline saved", Toast.LENGTH_SHORT).show();
                return;
            }

          // Create the download request
            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(downloadLink));
            request.setDescription("wait ...");
            request.setTitle(title);
            // in order for this if to run, you must use the android 3.2 to compile your app
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                request.allowScanningByMediaScanner();
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            }
            request.setDestinationInExternalFilesDir(context,Environment.DIRECTORY_DOWNLOADS, filename);

            // get download service and enqueue file
            final DownloadManager manager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);

            // Show progress dialog while downloading
            final ProgressDialog progDailog = new ProgressDialog(context);
            //Toast.makeText(context,"Download Manager",Toast.LENGTH_LONG).show();
            final long downloadId = manager.enqueue(request);
            new Thread(new Runnable() {

                @Override
                public void run() {

                    boolean downloading = true;

                    while (downloading) {

                        DownloadManager.Query q = new DownloadManager.Query();
                        q.setFilterById(downloadId);

                        Cursor cursor = manager.query(q);
                        cursor.moveToFirst();
                        int bytes_downloaded = cursor.getInt(cursor
                                .getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
                        int bytes_total = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));

                        if (cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)) == DownloadManager.STATUS_SUCCESSFUL) {
                            downloading = false;
                        }

                        final int dl_progress = (int) ((bytes_downloaded * 100l) / bytes_total);

                        basic_activity.runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                progDailog.setMessage("\t\t\t\t\t\t"+String.valueOf(dl_progress) + " %");

                            }
                        });

                        cursor.close();
                    }

                }
            }).start();

            BroadcastReceiver onComplete = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    if ( !progDailog.isShowing() ) {
                        return;
                    }
                    context.unregisterReceiver( this );

                    progDailog.dismiss();
                    long downloadId = intent.getLongExtra( DownloadManager.EXTRA_DOWNLOAD_ID, -1 );
                    Cursor c = manager.query( new DownloadManager.Query().setFilterById( downloadId ) );
                    downloadId1=downloadId;
                    if ( c.moveToFirst() ) {
                        int status = c.getInt( c.getColumnIndex( DownloadManager.COLUMN_STATUS ) );
                        if ( status == DownloadManager.STATUS_SUCCESSFUL ) {
                            Toast.makeText(context, "Offline saved", Toast.LENGTH_SHORT).show();
                            //openPDF( context, filename );
                        }
                    }
                    c.close();
                }
            };

            context.registerReceiver( onComplete, new IntentFilter( DownloadManager.ACTION_DOWNLOAD_COMPLETE ) );
            //manager.enqueue(request);

            progDailog.setTitle("Downloading Started ...");
            progDailog.setCancelable(false);
            progDailog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    manager.remove(downloadId);
                    dialog.dismiss();

                }
            });
            progDailog.show();


        }catch (Exception ex){
        }finally {
        }
    }

    /**
     * Show a dialog asking the user if he wants to open the PDF through Google Drive
     * @param context
     * @param pdfUrl
     */
    public static void askToOpenPDFThroughGoogleDrive( final Context context,final String title, final String pdfUrl ) {
        new AlertDialog.Builder( context )
                .setTitle( title )
                .setMessage( R.string.pdf_show_online_dialog_question )
                .setNegativeButton( R.string.pdf_show_online_dialog_button_no, null )
                .setPositiveButton( R.string.pdf_show_online_dialog_button_yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        openPDFThroughGoogleDrive(context, pdfUrl);
                    }
                })
                .show();
    }

    /**
     * Launches a browser to view the PDF through Google Drive
     * @param context
     * @param pdfUrl
     */
    public static void openPDFThroughGoogleDrive(final Context context, final String pdfUrl) {

        Intent i = new Intent( Intent.ACTION_VIEW );
        i.setDataAndType(parse(pdfUrl ), HTML_MIME_TYPE );
        context.startActivity( i );
    }
    /**
     * Open a local PDF file with an installed reader
     * @param context
     * @param filename
     */
    public static final void openPDF(Context context,String filename ) {
            Intent intent = new Intent(context, PdfViewerActivity.class);
            intent.putExtra("keyName",filename);
            context.startActivity(intent);
            return;

    }
    /**
     * Checks if any apps are installed that supports reading of PDF files.
     * @param context
     * @return
     */
    public static boolean isPDFSupported( Context context ) {
        Intent i = new Intent( Intent.ACTION_VIEW );
        final File tempFile = new File( context.getExternalFilesDir( Environment.DIRECTORY_DOWNLOADS ), "test.pdf" );
        i.setDataAndType( fromFile( tempFile ), PDF_MIME_TYPE );
        return context.getPackageManager().queryIntentActivities( i, PackageManager.MATCH_DEFAULT_ONLY ).size() > 0;
    }
}

