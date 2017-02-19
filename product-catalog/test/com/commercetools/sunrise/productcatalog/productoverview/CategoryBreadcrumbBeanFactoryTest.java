package com.commercetools.sunrise.productcatalog.productoverview;

import com.commercetools.sunrise.productcatalog.TestableProductReverseRouter;
import com.commercetools.sunrise.common.models.BreadcrumbBean;
import com.commercetools.sunrise.common.models.BreadcrumbLinkBean;
import com.commercetools.sunrise.productcatalog.productoverview.viewmodels.CategoryBreadcrumbBeanFactory;
import io.sphere.sdk.categories.Category;
import io.sphere.sdk.categories.CategoryTree;
import io.sphere.sdk.categories.queries.CategoryQuery;
import org.junit.Test;

import java.util.List;
import java.util.Locale;
import java.util.function.Consumer;

import static com.commercetools.sunrise.common.utils.JsonUtils.readCtpObject;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;

public class CategoryBreadcrumbBeanFactoryTest {

    private static final CategoryTree CATEGORY_TREE = CategoryTree.of(readCtpObject("breadcrumb/breadcrumbCategories.json", CategoryQuery.resultTypeReference()).getResults());

    @Test
    public void createCategoryBreadcrumbOfOneLevel() {
        testCategoryBreadcrumb("1stLevel",
                texts -> assertThat(texts).containsExactly("1st Level"),
                urls -> assertThat(urls).containsExactly("1st-level"));
    }

    @Test
    public void createCategoryBreadcrumbOfManyLevels() {
        testCategoryBreadcrumb("3rdLevel",
                texts -> assertThat(texts).containsExactly("1st Level", "2nd Level", "3rd Level"),
                urls -> assertThat(urls).containsExactly("1st-level", "2nd-level", "3rd-level"));
    }

    private void testCategoryBreadcrumb(final String extId, final Consumer<List<String>> texts, final Consumer<List<String>> urls) {
        final Category category = CATEGORY_TREE.findByExternalId(extId).get();
        final ProductsWithCategory data = ProductsWithCategory.of(null, category);
        final BreadcrumbBean breadcrumb = createBreadcrumbFactory().create(data);
        testBreadcrumb(breadcrumb, texts, urls);
    }

    private void testBreadcrumb(final BreadcrumbBean breadcrumb, final Consumer<List<String>> texts, final Consumer<List<String>> urls) {
        texts.accept(breadcrumb.getLinks().stream()
                .map(BreadcrumbLinkBean::getText)
                .map(link -> link.get(Locale.ENGLISH))
                .collect(toList()));
        urls.accept(breadcrumb.getLinks().stream().map(BreadcrumbLinkBean::getUrl).collect(toList()));
    }

    private static CategoryBreadcrumbBeanFactory createBreadcrumbFactory() {
        return new CategoryBreadcrumbBeanFactory(CATEGORY_TREE, new TestableProductReverseRouter());
    }
}
