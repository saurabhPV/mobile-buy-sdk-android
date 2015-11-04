/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 Shopify Inc.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.shopify.buy.dataprovider;

import android.content.Context;

import com.shopify.buy.dataprovider.tasks.SearchProductsTask;
import com.shopify.buy.model.Product;

import java.util.List;

import retrofit.Callback;

public class DefaultSearchProvider extends BaseProviderImpl implements SearchProvider {

    public DefaultSearchProvider(Context context) {
        super(context);
    }

    private SearchProductsTask task;

    @Override
    public void searchProducts(String query, BuyClient buyClient, Callback<List<Product>> callback) {
        if (task != null) {
            task.cancel();
        }
        task = new SearchProductsTask(query, buyDatabase, buyClient, callback, handler, executorService);
        executorService.execute(task);
    }

}
