import Header from "../Header/Header";
import { useEffect, useState } from "react";
import axios from 'axios';
import './styles/style.css';
import AddProduct from '../subComponenets/AddProduct';

const SupplierDashboard = () => {
    const host = localStorage.getItem("host");
    const [loginuser, setLoginuser] = useState('');
    const [ProductList, setProductList] = useState('');
    const url = "http://" + host;

    useEffect(() => {
        GetLoginDetails();
    }, [])

    axios.get(url + "/products").then(res => {
        setProductList(res.data);
    })

    async function GetLoginDetails() {
        const user = await axios.get(url + "/identities");
        console.log(user.data);
        setLoginuser(user.data);

    }


    return (
        <div>
            <Header />
            <section className="breadcrumb-section">
                <div className="container-fluid">
                    <div className="row breadcrumb-content py-2">
                        <nav aria-label="breadcrumb"> 

                            <a className="breadcrumb-item" href="#">Dashboard</a>

                            <p style={{ textAlign: 'right', fontStyle: 'italic' }}>Logged in as, <b>{loginuser}</b></p>
                        </nav>
                    </div>
                </div>
            </section>
            <main id="main">
                <section className="participants mt-4 mx-4">
                    <div className="container-fluid">
                        <div className="row">
                            <div className="table-block bg-white p-4 mb-5 rounded">
                                <div className="block-header d-flex justify-content-between">
                                    <div className="count-block d-flex">
                                        <div className="count main-color m-auto">
                                            <h5 className="helvetica-medium text-uppercase">ALL PRODUCTS</h5>
                                            <div className="fs-1">Total Product: {ProductList.length}</div>
                                        </div>
                                    </div>
                                    <div className="button-block">
                                        <a href="/all-products" className="btn btn-outline-primary">Update</a>
                                    </div>   
                                </div>
                            </div>
                        </div>
                    </div>
                </section>

            </main>
        </div>
    )
}
export default SupplierDashboard;