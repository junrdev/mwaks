package ke.ac.mwaks.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Recycler
import ke.ac.mwaks.R

class Account_activity : Fragment() {

    private lateinit var mlockUploads: RelativeLayout
    private lateinit var mlockDownloads: RelativeLayout
    private lateinit var mlockaccountSummary: RelativeLayout
    private lateinit var mlockOthers: RelativeLayout
    private lateinit var munlockFunctionalitiesBtn: CardView

    private lateinit var toolsRecycler: RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_account_activity, container, false)
        mlockUploads = view.findViewById(R.id.lockUploads)
        mlockDownloads = view.findViewById(R.id.lockDownloads)
        mlockaccountSummary = view.findViewById(R.id.lockaccountSummary)
        mlockOthers = view.findViewById(R.id.lockOthers)
        munlockFunctionalitiesBtn = view.findViewById(R.id.unlockFunctionalitiesBtn)

        mlockUploads.setOnClickListener { showOpenAccountToast() }
        mlockDownloads.setOnClickListener { showOpenAccountToast() }
        mlockaccountSummary.setOnClickListener { showOpenAccountToast() }
        mlockOthers.setOnClickListener { showOpenAccountToast() }
        munlockFunctionalitiesBtn.setOnClickListener {

            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.accountActivityFragment, OnBoarding())
            transaction.addToBackStack(null)
            transaction.commit()
        }

        //initialize fields

        return view
    }

    fun showOpenAccountToast() {
        Toast.makeText(
            requireActivity().applicationContext,
            "Unlock account to explore",
            Toast.LENGTH_SHORT
        ).show()
    }

}

