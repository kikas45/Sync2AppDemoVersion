package sync2app.com.syncapplive.additionalSettings.ApiUrls

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import sync2app.com.syncapplive.databinding.ItemApiUrlsRowsBinding


class SavedApiAdapter(
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<SavedApiAdapter.MyViewHolder>() {

    private var domainUrlList = emptyList<DomainUrl>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemApiUrlsRowsBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MyViewHolder(binding)
    }

    interface OnItemClickListener {
        fun onItemClicked(domainUrl: DomainUrl)
    }

    inner class MyViewHolder(private val binding: ItemApiUrlsRowsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val fileNameTextView = binding.textName

        init {
            binding.root.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val item = domainUrlList[position]
                    listener.onItemClicked(item)

                }
            }
        }

    }


    override fun getItemCount(): Int = domainUrlList.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val domainUrl = domainUrlList[position]
        holder.fileNameTextView.text = domainUrl.name
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(domainUrls: List<DomainUrl>) {
        domainUrlList = domainUrls
        notifyDataSetChanged()
    }
}
