class File {
    private String name;
    private int[] content;

    public File(int[] content) {
        this.content = content;
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