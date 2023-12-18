package remotex.com.remotewebview.additionalSettings.DownloadsArray.list

import android.annotation.SuppressLint
import android.app.DownloadManager
import android.content.Context
import android.content.Context.DOWNLOAD_SERVICE
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import remotex.com.remotewebview.R
import remotex.com.remotewebview.additionalSettings.DownloadsArray.model.DownloadModel
import remotex.com.remotewebview.additionalSettings.DownloadsArray.viewmodel.DownloadViewModel
import remotex.com.remotewebview.databinding.ItemDownloadRowsBinding


class DownloadListAdapter(

    private val listener: OnItemClickListener,
    private val onLongListener: OnItemLongClickListener,
    private val mUserViewModel: DownloadViewModel,
    private val context: Context
): RecyclerView.Adapter<DownloadListAdapter.MyViewHolder>() {

    private var userList = emptyList<DownloadModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemDownloadRowsBinding.bind(
            LayoutInflater.from(parent.context).inflate(R.layout.item_download_rows, parent, false)
        )

        return MyViewHolder(binding)
    }

    interface OnItemClickListener {
        fun onItemClicked(photo: DownloadModel)
    }

    interface OnItemLongClickListener {
        fun onItemLongClicked(photo: DownloadModel)
    }

    inner class MyViewHolder(private val binding: ItemDownloadRowsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val fileNameTextView = binding.textTitleDownload
        val progressBarPref: ProgressBar = binding.progressBarPref
        val textFileSize: TextView = binding.textFileSieze
        val moreFav: ImageView = binding.moreFav

        init {
            binding.moreFav.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val item = userList[position]
                    listener.onItemClicked(item)
                }
            }

            binding.root.setOnLongClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val item = userList[position]
                    if (item != null) {
                        onLongListener.onItemLongClicked(item)
                        return@setOnLongClickListener true
                    }
                }
                return@setOnLongClickListener false
            }
        }

    }

    override fun getItemCount(): Int {
        return userList.size
    }


    @SuppressLint("Range")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = userList[position]
        holder.fileNameTextView.text = currentItem.title.uppercase()
        holder.textFileSize.text = currentItem.pathName

        if (currentItem.downloadId != -1L) {
            val downloadManager = context.getSystemService(DOWNLOAD_SERVICE) as DownloadManager
            val query = DownloadManager.Query().setFilterById(currentItem.downloadId)
            val cursor = downloadManager.query(query)

            if (cursor.moveToFirst()) {
                val progress =
                    cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR))
                val total =
                    cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES))

                val percentage = if (total > 0) (progress * 100 / total).toInt() else 0

                holder.progressBarPref.progress = percentage

                // Update the download progress in the repository
                mUserViewModel.updateDownloadProgress(
                    currentItem.id,
                    progress.toLong(),
                    total.toLong()
                )

            }

            cursor.close()
        } else {
            holder.progressBarPref.progress = 0  // Set to 0 if downloadId is not available

        }
    }


    @SuppressLint("NotifyDataSetChanged")
    fun setData(user: List<DownloadModel>) {
        this.userList = user
        notifyDataSetChanged()
    }

}
