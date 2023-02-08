import Header from "../Header/Header";

const SupplierDashboard=()=>{
    const currentuser=localStorage.getItem("currrentuser");
    return(
        <main>
            <Header />
        </main>
    )
}
export default SupplierDashboard;