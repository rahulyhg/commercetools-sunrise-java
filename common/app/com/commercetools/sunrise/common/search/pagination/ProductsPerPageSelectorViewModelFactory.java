package com.commercetools.sunrise.common.search.pagination;

import com.commercetools.sunrise.framework.injection.RequestScoped;
import com.commercetools.sunrise.common.models.ViewModelFactory;
import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.queries.PagedResult;
import play.mvc.Http;

import javax.annotation.Nullable;
import javax.inject.Inject;

import static com.commercetools.sunrise.common.forms.QueryStringUtils.findSelectedValueFromQueryString;
import static java.util.stream.Collectors.toList;

@RequestScoped
public class ProductsPerPageSelectorViewModelFactory extends ViewModelFactory<ProductsPerPageSelectorViewModel, PagedResult<ProductProjection>> {

    @Nullable
    private final String selectedOptionValue;
    private final ProductsPerPageFormSettings settings;
    private final ProductsPerPageFormSelectableOptionViewModelFactory productsPerPageFormSelectableOptionViewModelFactory;

    @Inject
    public ProductsPerPageSelectorViewModelFactory(final ProductsPerPageFormSettings settings, final Http.Request httpRequest,
                                                   final ProductsPerPageFormSelectableOptionViewModelFactory productsPerPageFormSelectableOptionViewModelFactory) {
        this.selectedOptionValue = findSelectedValueFromQueryString(settings, httpRequest)
                .map(ProductsPerPageFormOption::getFieldValue)
                .orElse(null);
        this.settings = settings;
        this.productsPerPageFormSelectableOptionViewModelFactory = productsPerPageFormSelectableOptionViewModelFactory;
    }

    @Override
    protected ProductsPerPageSelectorViewModel getViewModelInstance() {
        return new ProductsPerPageSelectorViewModel();
    }

    @Override
    public final ProductsPerPageSelectorViewModel create(final PagedResult<ProductProjection> pagedResult) {
        return super.create(pagedResult);
    }

    @Override
    protected final void initialize(final ProductsPerPageSelectorViewModel viewModel, final PagedResult<ProductProjection> pagedResult) {
        fillKey(viewModel, pagedResult);
        fillList(viewModel, pagedResult);
    }

    protected void fillKey(final ProductsPerPageSelectorViewModel model, final PagedResult<ProductProjection> pagedResult) {
        model.setKey(settings.getFieldName());
    }

    protected void fillList(final ProductsPerPageSelectorViewModel model, final PagedResult<ProductProjection> pagedResult) {
        model.setList(settings.getOptions().stream()
                .map(option -> productsPerPageFormSelectableOptionViewModelFactory.create(option, selectedOptionValue))
                .collect(toList()));
    }
}