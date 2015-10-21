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

package com.shopify.buy.ui.cart;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.shopify.buy.R;
import com.shopify.buy.model.Cart;
import com.shopify.buy.model.CartLineItem;
import com.shopify.buy.model.Shop;
import com.shopify.buy.ui.common.BaseFragment;
import com.shopify.buy.utils.CurrencyFormatter;

import java.text.NumberFormat;
import java.util.Locale;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class CartFragment extends BaseFragment {

    protected Cart cart;
    protected NumberFormat currencyFormat;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();

        // Retrieve the cart if it was provided
        String cartJsonString = bundle.getString(CartConfig.EXTRA_CART);
        if (!TextUtils.isEmpty(cartJsonString)) {
            cart = Cart.fromJson(cartJsonString);
        }

        fetchShopIfNecessary(new Callback<Shop>() {
            @Override
            public void success(Shop shop, Response response) {
                currencyFormat = CurrencyFormatter.getFormatter(Locale.getDefault(), shop.getCurrency());
                showCartIfReady();
            }

            @Override
            public void failure(RetrofitError error) {
                // TODO
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.cart_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        showCartIfReady();
    }

    private void showCartIfReady() {
        if (currencyFormat == null) {
            return;
        }

        final ArrayAdapter<CartLineItem> adapter = new ArrayAdapter<CartLineItem>(getActivity(), R.layout.cart_line_item_view, cart.getLineItems()) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = convertView;

                if (view == null) {
                    view = View.inflate(getContext(), R.layout.cart_line_item_view, null);
                }

                ((CartLineItemView) view).setLineItem(getItem(position), currencyFormat);

                return view;
            }
        };

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ((ListView) getView().findViewById(R.id.cart_list_view)).setAdapter(adapter);
            }
        });
    }

}