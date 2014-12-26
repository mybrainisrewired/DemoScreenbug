package com.google.android.gms.drive.query;

import com.google.android.gms.drive.metadata.SearchableCollectionMetadataField;
import com.google.android.gms.drive.metadata.SearchableMetadataField;
import com.google.android.gms.drive.query.internal.ComparisonFilter;
import com.google.android.gms.drive.query.internal.FieldOnlyFilter;
import com.google.android.gms.drive.query.internal.InFilter;
import com.google.android.gms.drive.query.internal.LogicalFilter;
import com.google.android.gms.drive.query.internal.NotFilter;
import com.google.android.gms.drive.query.internal.Operator;

public class Filters {
    public static Filter and(Filter filter, Filter... additionalFilters) {
        return new LogicalFilter(Operator.GZ, filter, additionalFilters);
    }

    public static Filter and(Iterable<Filter> filters) {
        return new LogicalFilter(Operator.GZ, filters);
    }

    public static Filter contains(SearchableMetadataField field, Object value) {
        return new ComparisonFilter(Operator.Hc, field, value);
    }

    public static <T> Filter eq(SearchableMetadataField field, Object value) {
        return new ComparisonFilter(Operator.GU, field, value);
    }

    public static <T extends Comparable<T>> Filter greaterThan(SearchableMetadataField field, Object value) {
        return new ComparisonFilter(Operator.GX, field, value);
    }

    public static <T extends Comparable<T>> Filter greaterThanEquals(SearchableMetadataField field, Object value) {
        return new ComparisonFilter(Operator.GY, field, value);
    }

    public static <T> Filter in(SearchableCollectionMetadataField field, Object value) {
        return new InFilter(field, value);
    }

    public static <T extends Comparable<T>> Filter lessThan(SearchableMetadataField field, Object value) {
        return new ComparisonFilter(Operator.GV, field, value);
    }

    public static <T extends Comparable<T>> Filter lessThanEquals(SearchableMetadataField field, Object value) {
        return new ComparisonFilter(Operator.GW, field, value);
    }

    public static Filter not(Filter toNegate) {
        return new NotFilter(toNegate);
    }

    public static Filter or(Filter filter, Filter... additionalFilters) {
        return new LogicalFilter(Operator.Ha, filter, additionalFilters);
    }

    public static Filter or(Iterable<Filter> filters) {
        return new LogicalFilter(Operator.Ha, filters);
    }

    public static Filter sharedWithMe() {
        return new FieldOnlyFilter(SearchableField.GE);
    }
}