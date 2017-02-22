package com.commercetools.sunrise.common.models.products;

import com.commercetools.sunrise.common.models.AttributeWithProductType;
import com.commercetools.sunrise.common.models.ViewModelFactory;
import com.commercetools.sunrise.framework.injection.RequestScoped;
import com.commercetools.sunrise.common.utils.AttributeFormatter;

import javax.inject.Inject;

@RequestScoped
public class ProductAttributeViewModelFactory extends ViewModelFactory<ProductAttributeViewModel, AttributeWithProductType> {

    private final AttributeFormatter attributeFormatter;

    @Inject
    public ProductAttributeViewModelFactory(final AttributeFormatter attributeFormatter) {
        this.attributeFormatter = attributeFormatter;
    }

    @Override
    protected ProductAttributeViewModel getViewModelInstance() {
        return new ProductAttributeViewModel();
    }

    @Override
    public final ProductAttributeViewModel create(final AttributeWithProductType attributeWithProductType) {
        return super.create(attributeWithProductType);
    }

    @Override
    protected final void initialize(final ProductAttributeViewModel viewModel, final AttributeWithProductType attributeWithProductType) {
        fillKey(viewModel, attributeWithProductType);
        fillName(viewModel, attributeWithProductType);
        fillValue(viewModel, attributeWithProductType);
    }

    protected void fillKey(final ProductAttributeViewModel model, final AttributeWithProductType attributeWithProductType) {
        model.setKey(attributeWithProductType.getAttribute().getName());
    }

    protected void fillName(final ProductAttributeViewModel model, final AttributeWithProductType attributeWithProductType) {
        model.setName(attributeFormatter.label(attributeWithProductType));
    }

    protected void fillValue(final ProductAttributeViewModel model, final AttributeWithProductType attributeWithProductType) {
        model.setValue(attributeFormatter.value(attributeWithProductType));
    }
}