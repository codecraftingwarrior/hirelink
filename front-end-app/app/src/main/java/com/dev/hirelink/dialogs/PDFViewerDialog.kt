package com.dev.hirelink.dialogs

import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.dev.hirelink.R
import com.dev.hirelink.components.getRootDirPath
import com.dev.hirelink.models.Document
import com.downloader.Error
import com.downloader.OnDownloadListener
import com.downloader.PRDownloader
import com.github.barteksc.pdfviewer.PDFView
import com.google.android.material.progressindicator.CircularProgressIndicator
import java.io.File

class PDFViewerDialog(
    private val document: Document
) : DialogFragment() {
    private lateinit var inflatedView: View

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)

            val inflater = requireActivity().layoutInflater

            inflatedView = inflater.inflate(R.layout.document_viewer_layout, null)

            builder
                .setTitle(document.title)
                .setView(inflatedView)

            bindListeners(inflatedView)

            setupLayout()

            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    private fun setupLayout() {
        val fileName = document.fileName
        downloadPdfFromInternet(
            document.getFullPath(),
            getRootDirPath(requireContext()),
            fileName!!
        )
    }

    private fun downloadPdfFromInternet(url: String, dirPath: String, fileName: String) {
        showLoader()
        PRDownloader
            .download(url, dirPath, fileName)
            .build()
            .start(object : OnDownloadListener {
                override fun onDownloadComplete() {
                    val downloadedFile = File(dirPath, fileName)
                    hideLoader()
                    showPdfFromFile(downloadedFile)
                }

                override fun onError(error: Error?) {
                    hideLoader()
                    Toast.makeText(
                        requireActivity(),
                        "Error during downloading file : $error",
                        Toast.LENGTH_LONG
                    )
                        .show()
                }
            })
    }

    private fun bindListeners(inflatedView: View) {
        with(inflatedView) {

        }
    }

    private fun showLoader() {
        inflatedView.findViewById<PDFView>(R.id.pdf_view_any_document).visibility = View.GONE
        inflatedView.findViewById<CircularProgressIndicator>(R.id.progress_bar_downloading).visibility = View.VISIBLE
    }

    private fun hideLoader() {
        inflatedView.findViewById<PDFView>(R.id.pdf_view_any_document).visibility = View.VISIBLE
        inflatedView.findViewById<CircularProgressIndicator>(R.id.progress_bar_downloading).visibility = View.GONE
    }

    private fun showPdfFromFile(file: File) {
        inflatedView
            .findViewById<PDFView>(R.id.pdf_view_any_document)
            .fromFile(file)
            .password(null)
            .defaultPage(0)
            .enableSwipe(true)
            .swipeHorizontal(false)
            .enableDoubletap(true)
            .onPageError { page, _ ->
                Toast.makeText(
                    requireActivity(),
                    "Error at page: $page", Toast.LENGTH_LONG
                ).show()
            }
            .load()
    }


}