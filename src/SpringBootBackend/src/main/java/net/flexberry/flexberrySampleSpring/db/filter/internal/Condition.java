package net.flexberry.flexberrySampleSpring.db.filter.internal;

public class Condition {
    public DataType dataType;
    public CompareType compareType;

    public Object value;

    public String field;

    public Condition() {
    }

    public Condition(DataType dataType, CompareType compareType, Object value, String field) {
        this.dataType = dataType;
        this.compareType = compareType;
        this.value = value;
        this.field = field;
    }

    public static class Builder {
        private DataType dataType;
        private CompareType compareType;
        private Object value;
        private String field;

        public Builder setType(DataType dataType) {
            this.dataType = dataType;
            return this;
        }

        public Builder setComparison(CompareType compareType) {
            this.compareType = compareType;
            return this;
        }

        public Builder setValue(Object value) {
            this.value = value;
            return this;
        }

        public Builder setField(String field) {
            this.field = field;
            return this;
        }

        public Condition build() {
            return new Condition(dataType, compareType, value, field);
        }
    }
}
