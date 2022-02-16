package edu.rosehulman.roseride

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import edu.rosehulman.roseride.ui.historyList.HistoryListFragment
import edu.rosehulman.roseride.model.History
import edu.rosehulman.roseride.model.HistoryViewModel

class HistoryAdapter (fragment: HistoryListFragment) : RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {
    val model = ViewModelProvider(fragment.requireActivity()).get(HistoryViewModel::class.java)
    val fragment = fragment


    inner class HistoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        val title: TextView = itemView.findViewById(R.id.row_history_title)
        val dAddr: TextView = itemView.findViewById(R.id.row_history_address)
        val time: TextView = itemView.findViewById(R.id.row_history_set_off_time)
        val driver: TextView = itemView.findViewById(R.id.row_history_driver)
        val cost: TextView = itemView.findViewById(R.id.row_history_cost)

        init {

//            itemView.setOnClickListener {
//                // navigate
//                model.updateCurrentPos(adapterPosition)
//                itemView.findNavController().navigate(R.id.navigation_ride_detail, null,
//                    navOptions {
//                        anim {
//                            enter = android.R.anim.slide_in_left
//                            exit = android.R.anim.slide_out_right
//                        }
//                    })
//            }
        }

        fun bind(r: History) {

            title.text = r.title
            dAddr.text = "Address: "+r.destinationAddr.toString()
            time.text = "Set-off Time: "+ r.setOffDate.toString() + " " + r.setOffTime.toString().substring(0, r.setOffTime.toString().length-3)
            driver.text = "Driver: "+r.driver.toString()
            cost.text = "Cost/Person ($): "+r.costPerPerson.toString()

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.row_history, parent, false)
        return HistoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        holder.bind(model.getHistoryAt(position))
    }

    override fun getItemCount() = model.size()

    fun addListener(fragmentName: String) {
        model.addListener(fragmentName,{
            notifyDataSetChanged()
        })
    }

    fun removeListener(fragmentName: String) {
        model.removeListener(fragmentName)
    }

}