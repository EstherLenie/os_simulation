class File {
    private String name;
    private int[] content;

    public File(String name, int[] content) {
        this.content = content;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int[] getContent() {
        return content;
    }

    public int getSize(){
        return content.length * 4;
    }
}