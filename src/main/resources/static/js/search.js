function searchByTitleAndAuthor() {
    var select = document.getElementById("searchBy");
    var selectedOption = select.options[select.selectedIndex].text;
    var titleSearch = document.getElementById("titleSearch");
    var authorSearch = document.getElementById("authorSearch");
    if (selectedOption === "Title") {
        authorSearch.classList.add("hidden");
        titleSearch.classList.remove("hidden");
    } else if (selectedOption === "Author") {
        titleSearch.classList.add("hidden");
        authorSearch.classList.remove("hidden");
    } else if (selectedOption === "Title and Author") {
        authorSearch.classList.remove("hidden");
        titleSearch.classList.remove("hidden");
    }
    return;
}