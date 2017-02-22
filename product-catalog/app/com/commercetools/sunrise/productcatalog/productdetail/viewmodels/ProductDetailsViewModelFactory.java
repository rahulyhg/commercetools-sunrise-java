package com.commercetools.sunrise.productcatalog.productdetail.viewmodels;

import com.commercetools.sunrise.common.ctp.ProductAttributeSettings;
import com.commercetools.sunrise.common.models.products.ProductAttributeViewModel;
import com.commercetools.sunrise.common.models.products.ProductAttributeViewModelFactory;
import com.commercetools.sunrise.framework.injection.RequestScoped;
import com.commercetools.sunrise.common.models.*;
import com.commercetools.sunrise.productcatalog.productdetail.ProductWithVariant;
import io.sphere.sdk.models.Reference;
import io.sphere.sdk.products.attributes.Attribute;
import io.sphere.sdk.producttypes.ProductType;

import javax.inject.Inject;
import java.util.List;
import java.util.Objects;

import static java.util.stream.Collectors.toList;

@RequestScoped
public class ProductDetailsViewModelFactory extends ViewModelFactory<ProductDetailsViewModel, ProductWithVariant> {

    private final ProductAttributeSettings productAttributeSettings;
    private final ProductAttributeViewModelFactory productAttributeViewModelFactory;

    @Inject
    public ProductDetailsViewModelFactory(final ProductAttributeSettings productAttributeSettings, final ProductAttributeViewModelFactory productAttributeViewModelFactory) {
        this.productAttributeSettings = productAttributeSettings;
        this.productAttributeViewModelFactory = productAttributeViewModelFactory;
    }

    @Override
    public final ProductDetailsViewModel create(final ProductWithVariant productWithVariant) {
        return super.create(productWithVariant);
    }

    @Override
    protected ProductDetailsViewModel getViewModelInstance() {
        return new ProductDetailsViewModel();
    }

    @Override
    protected final void initialize(final ProductDetailsViewModel viewModel, final ProductWithVariant productWithVariant) {
        fillList(viewModel, productWithVariant);
    }

    protected void fillList(final ProductDetailsViewModel model, final ProductWithVariant productWithVariant) {
        final List<ProductAttributeViewModel> attributes = productAttributeSettings.getDisplayedAttributes().stream()
                .map(productWithVariant.getVariant()::getAttribute)
                .filter(Objects::nonNull)
                .map(attribute -> createProductAttributeViewModel(productWithVariant, attribute))
                .collect(toList());
        model.setFeatures(attributes);
    }

    private ProductAttributeViewModel createProductAttributeViewModel(final ProductWithVariant productWithVariant, final Attribute attribute) {
        final Reference<ProductType> productTypeRef = productWithVariant.getProduct().getProductType();
        return productAttributeViewModelFactory.create(AttributeWithProductType.of(attribute, productTypeRef));
    }
}