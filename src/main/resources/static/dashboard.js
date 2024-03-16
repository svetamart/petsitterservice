function showPetDetails(petId) {
    var petDetails = document.getElementById('pet-details-' + petId);
    if (petDetails.style.display === 'none') {
        petDetails.style.display = 'block';
    } else {
        petDetails.style.display = 'none';
    }
}