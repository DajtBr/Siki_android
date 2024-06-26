package com.example.siki.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.siki.Adapter.ProductListForCustomerRecycleAdapter;
import com.example.siki.R;
import com.example.siki.database.CategoryDatabase;
import com.example.siki.database.ProductCategoryDatabase;
import com.example.siki.database.PromotionDataSource;
import com.example.siki.model.Category;
import com.example.siki.model.Product;
import com.example.siki.variable.GlobalVariable;

import java.util.ArrayList;
import java.util.List;

public class ListProductForCustomerActivity extends AppCompatActivity {
    private List<Product> productList = new ArrayList<>();
    TextView tv_categoryName;
    RecyclerView rv_product_list_customer;
    private ProductCategoryDatabase productCategoryDatabase;

    private PromotionDataSource promotionDataSource;
    private CategoryDatabase categoryDatabase;
    private Category category = new Category();
    private Long categoryId;
    private Button btn_list_product_back;

    private ProductListForCustomerRecycleAdapter productListForCustomerRecycleAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_product_for_customer);
        setControl();
        setEvent();
        GlobalVariable globalVariable = (GlobalVariable) getApplication();
        productListForCustomerRecycleAdapter = new ProductListForCustomerRecycleAdapter(productList, this, globalVariable);
        rv_product_list_customer.setAdapter(productListForCustomerRecycleAdapter);
        rv_product_list_customer.setLayoutManager(new GridLayoutManager(this, 2));
    }

    private void setEvent() {
        btn_list_product_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ListProductForCustomerActivity.this, HomeActivity.class);
                intent.putExtra("fragment", R.id.nav_home);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        readDb();
    }

    private void readDb() {
        // get list product by category id
        categoryDatabase = new CategoryDatabase(this);
        categoryDatabase.open();
        productCategoryDatabase = new ProductCategoryDatabase(this);
        productCategoryDatabase.open();

        promotionDataSource = new PromotionDataSource(this);
        promotionDataSource.open();

        productList.clear();
        category = categoryDatabase.findById(categoryId);
        if (category != null) {
            tv_categoryName.setText(category.getName());
        }
        productList.addAll(productCategoryDatabase.findByCategoryId(categoryId, promotionDataSource));
        productListForCustomerRecycleAdapter.notifyDataSetChanged();
    }

    private void setControl() {
        tv_categoryName = findViewById(R.id.tv_categoryName);
        btn_list_product_back = findViewById(R.id.btn_list_product_back);
        rv_product_list_customer = findViewById(R.id.rv_product_list_customer);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
             categoryId = extras.getLong("categoryId");
        }
    }
}