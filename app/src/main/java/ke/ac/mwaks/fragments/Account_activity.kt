package ke.ac.mwaks.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import ke.ac.mwaks.R

class Account_activity : Fragment() {

    private lateinit var uploads : TextView
    private lateinit var downloads : TextView
    private lateinit var conversion : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

         val view = inflater.inflate(R.layout.fragment_account_activity, container, false)


            uploads = view.findViewById(R.id.userUploads)
            downloads = view.findViewById(R.id.userDownloads)
            conversion = view.findViewById(R.id.conversionRate)


        //initialize fields

        return view
    }

}