package com.example.yuki.firebasetraining

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    lateinit var editTextName: EditText
    lateinit var ratingBar: RatingBar
    lateinit var  button: Button

    lateinit var  heroList: MutableList<Hero>
    lateinit var  ref: DatabaseReference
    lateinit var  listView: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        heroList = mutableListOf()
        ref = FirebaseDatabase.getInstance().getReference("heroes")
        editTextName = findViewById(R.id.editTextName)
        ratingBar = findViewById(R.id.ratingBar)
        button = findViewById(R.id.buttonSave)
        listView = findViewById(R.id.listView)


        buttonSave.setOnClickListener{

            saveHero()
        }

        ref.addValueEventListener(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(p0: DataSnapshot) {
              if(p0!!.exists()){

                  heroList.clear()
                  
                  for(h in p0.children){
                      val hero = h.getValue(Hero::class.java)
                      heroList.add(hero!!)
                  }

                  val adapter = HeroAdapter(applicationContext, R.layout.heros, heroList)
                  listView.adapter = adapter
              }

            }

        })

    }


    private fun saveHero(){
        val name = editTextName.text.toString().trim()

        if(name.isEmpty()){
            editTextName.error = "Please enter a name"
            return
        }


        val heroId = ref.push().key

        val hero = Hero(heroId.toString(),name,ratingBar.rating.toInt())
        ref.child(heroId.toString()).setValue(hero).addOnCanceledListener {
            Toast.makeText(applicationContext, "Hero",Toast.LENGTH_SHORT)
        }

    }
}
