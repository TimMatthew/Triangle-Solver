public abstract class Shape {

    String id;

    public Shape(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object obj) {

        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        Shape other = (Shape) obj;
        return id != null && getId().equals(other.getId());
    }
}
