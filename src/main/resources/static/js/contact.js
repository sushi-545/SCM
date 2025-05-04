console.log("Contacts.js");
// const baseURL = "http://localhost:8081";
const baseURL = "https://www.scm20.site";
const viewContactModal = document.getElementById("view_contact_modal");
console.log("viewContactModal:", viewContactModal);
// Declare contactModal first
let contactModal;

// options with default values
const options = {
  placement: "bottom-right",
  backdrop: "dynamic",
  backdropClasses: "bg-gray-900/50 dark:bg-gray-900/80 fixed inset-0 z-40",
  closable: true,
  onHide: () => {
    console.log("modal is hidden");
  },
  onShow: () => {
    setTimeout(() => {
      // Now contactModal should be initialized
      contactModal.classList.add("scale-100");
    }, 50);
  },
  onToggle: () => {
    console.log("modal has been toggled");
  },
};

// instance options object
const instanceOptions = {
  id: "view_contact_modal",
  override: true,
};

// Initialize contactModal after the options object
//contactModal = new Modal(viewContactModal, options, instanceOptions);
//console.log("contactModal:", contactModal);

function openContactModal() {
  console.log("openContactModal called");
  contactModal= new Modal(viewContactModal, options, instanceOptions);
  if (contactModal) {
    contactModal.show();
  } else {
    console.error("contactModal is undefined in openContactModal!");
  }}
async function loadContactData(id) {
    console.log(id);

    try{

    const data = await (await fetch(`http://localhost:8080/api/contacts/${id}`)).json();
    console.log(data);
     document.querySelector("#contact_name").innerHTML = data.name;
     document.querySelector("#contact_email").innerHTML = data.email;
        openContactModal();
    }catch (error) {
         console.log("Error: ", error);
       }

 }
 function closeContactModal() {
    console.log("openContactModal called");
    contactModal= new Modal(viewContactModal, options, instanceOptions);
    if (contactModal) {
      contactModal.hide();
    } else {
      console.error("contactModal is undefined in openContactModal!");
    }
  }

  async function deleteContact(id) {
    Swal.fire({
      title: "Do you want to delete the contact?",
      icon: "warning",
      showCancelButton: true,
      confirmButtonText: "Delete",
    }).then((result) => {
      /* Read more about isConfirmed, isDenied below */
      if (result.isConfirmed) {
        console.log("id::::",id);
        const url = `http://localhost:8080/user/contact/delete/` + id;
        window.location.replace(url);
      }
    });
  }