package sync2app.com.syncapplive.additionalSettings.myCompleteDownload

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import sync2app.com.syncapplive.R
import sync2app.com.syncapplive.databinding.ItemSavedDownloadRowsBinding

class SavedDownloads(): RecyclerView.Adapter<SavedDownloads.MyViewHolder>() {

    private var userList = emptyList<DnApi>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemSavedDownloadRowsBinding.bind(
            LayoutInflater.from(parent.context).inflate(R.layout.item_saved_download_rows, parent, false)
        )

        return MyViewHolder(binding)
    }


    inner class MyViewHolder(private val binding: ItemSavedDownloadRowsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val fileName = binding.fileName
        val filePath = binding.filePath


    }

    override fun getItemCount(): Int {
        return userList.size
    }


    @SuppressLint("Range")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = userList[position]

        val filename = currentItem.FileName
        val folderName  = currentItem.FolderName
        val Sn  = currentItem.SN

        val title = "$folderName/ $filename"
        holder.fileName.text = " $Sn: $filename"
        holder.filePath.text = title

        }


    @SuppressLint("NotifyDataSetChanged")
    fun setDataApi(user: List<DnApi>){
        this.userList = user
        notifyDataSetChanged()
    }



}
