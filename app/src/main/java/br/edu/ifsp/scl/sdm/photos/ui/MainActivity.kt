package br.edu.ifsp.scl.sdm.photos.ui

import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import br.edu.ifsp.scl.sdm.photos.R
import br.edu.ifsp.scl.sdm.photos.adapter.ProductAdapter
import br.edu.ifsp.scl.sdm.photos.adapter.ProductImageAdapter
import br.edu.ifsp.scl.sdm.photos.databinding.ActivityMainBinding
import br.edu.ifsp.scl.sdm.photos.model.DummyJSONAPI
import br.edu.ifsp.scl.sdm.photos.model.Product
import com.android.volley.toolbox.ImageRequest

class MainActivity : AppCompatActivity() {
    private val amb: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val productList: MutableList<Product> = mutableListOf()
    private val productAdapter: ProductAdapter by lazy {
        ProductAdapter(this, productList)
    }

    private val productImageList: MutableList<Bitmap> = mutableListOf()
    private val productImageAdapter: ProductImageAdapter by lazy {
        ProductImageAdapter(this, productImageList)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
         setContentView(amb.root)

        setSupportActionBar(amb.mainTb.apply {
            title = getString(R.string.app_name)
        })

        // Associar adapter ao spinner
        amb.productsSp.apply {
            adapter = productAdapter

            onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    val sizeImageList = productImageList.size
                    productImageList.clear()
                    productImageAdapter.notifyItemRangeRemoved(0, sizeImageList)
                    retrieveProductImages(productList[position])
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {

                }
            }

        }

        amb.productImagesRv.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = productImageAdapter
        }

        retrieveProducts()

    }

    // Populate spinner

    private fun retrieveProducts() = DummyJSONAPI.ProductListRequest({ productList ->
            productList.products.also {
                productAdapter.addAll(it)
            }
        }, {
            Toast.makeText(this, R.string.request_problem, Toast.LENGTH_SHORT).show()
        }).also {
        DummyJSONAPI.getInstance(this).addToRequestQueue(it)
    }

    private fun retrieveProductImages(product: Product) =
        product.images.forEach { imageUrl ->
            ImageRequest(imageUrl, { response ->
                productImageList.add(response)
                productImageAdapter.notifyItemInserted(productImageList.lastIndex)
            }, 0, 0, ImageView.ScaleType.CENTER, Bitmap.Config.ARGB_8888, {
                Toast.makeText(this, R.string.request_problem, Toast.LENGTH_SHORT).show()
            }).also {
                DummyJSONAPI.getInstance(this).addToRequestQueue(it)
            }
        }

}