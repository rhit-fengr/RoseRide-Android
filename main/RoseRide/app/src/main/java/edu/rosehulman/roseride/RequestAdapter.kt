package edu.rosehulman.roseride

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.navOptions
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.color.MaterialColors
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import edu.rosehulman.roseride.model.Request
import edu.rosehulman.roseride.model.RequestViewModel
import edu.rosehulman.roseride.ui.requestList.RequestListFragment

class RequestAdapter(fragment: RequestListFragment) : RecyclerView.Adapter<RequestAdapter.RequestViewHolder>() {
    val model = ViewModelProvider(fragment.requireActivity()).get(RequestViewModel::class.java)
    val fragment = fragment


    inner class RequestViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        val title: TextView = itemView.findViewById(R.id.row_request_title)
        val address: TextView = itemView.findViewById(R.id.row_request_address)
        val time: TextView = itemView.findViewById(R.id.row_request_set_off_time)
        val price: TextView = itemView.findViewById(R.id.row_request_price)
//        val editbtn: ImageButton =  itemView.findViewById(R.id.row_request_rightImage)
        val editbtn: ImageView =  itemView.findViewById(R.id.row_request_rightImage)


        init {

            itemView.setOnClickListener{
                // navigate
                model.updateCurrentPos(adapterPosition)
                itemView.findNavController().navigate(R.id.nav_request_detail, null,
                    navOptions {
                        anim {
                            enter = android.R.anim.slide_in_left
                            exit = android.R.anim.slide_out_right
                        }
                    })
            }

            editbtn.setOnClickListener {
                model.updateCurrentPos(adapterPosition)
                itemView.findNavController().navigate(R.id.nav_request_edit, null,
                    navOptions {
                        anim {
                            enter = android.R.anim.slide_in_left
                            exit = android.R.anim.slide_out_right
                        }
                    })
            }

            itemView.setOnLongClickListener{
                model.updateCurrentPos(adapterPosition)
                model.toggleCurrentRequest()
                notifyDataSetChanged()
//                notifyItemChanged(adapterPosition)
                true
            }
        }

        fun bind(r: Request) {

            if(MainActivity.driverMode || r.user != Firebase.auth.uid){
                editbtn.visibility=View.GONE
            }else{
                editbtn.visibility=View.VISIBLE
            }

            title.text = r.title
            address.text = "Address: "+r.destinationAddr.toString()
            time.text = "Set-off Time: "+ r.setOffDate.toString() + " " + r.setOffTime.toString().substring(0, r.setOffTime.toString().length-3)
            price.text = "Price($): "+r.minPrice+" ~ "+r.maxPrice
            Log.d("RR","isSelected: ${r.isSelected}")

            val selected = MaterialColors.getColor(
                fragment.requireContext(),
                R.attr.colorAccent,
                Color.WHITE
            )

            val unselected = MaterialColors.getColor(
                fragment.requireContext(),
                R.attr.colorSurface,
                Color.WHITE
            )

            val color = if (r.isSelected) selected else unselected
            itemView.setBackgroundColor(color)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RequestViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.row_request, parent, false)
        return RequestViewHolder(view)
    }

    override fun onBindViewHolder(holder: RequestViewHolder, position: Int) {
        holder.bind(model.getRequestAt(position))
    }

    override fun getItemCount() = model.size()

    fun addRequest(request: Request?) {
        model.addRequest(request)
        Log.d("RR","Size: ${model.size()}")
        notifyDataSetChanged()
    }

    fun addAllListener(fragmentName: String) {
        model.addAllListener(fragmentName,{
            notifyDataSetChanged()
        })
    }

    fun addOneListener(fragmentName: String) {
        model.addOneListener(fragmentName,{
            notifyDataSetChanged()
        })
    }

    fun removeListener(fragmentName: String) {
        model.removeListener(fragmentName)
    }

}