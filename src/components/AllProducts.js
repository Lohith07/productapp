import { useEffect, useState } from "react";
import Header from "./Header/Header";
import axios from 'axios';
import AddProduct from "./subComponenets/AddProduct";


export default function AllProducts() {
    const host = localStorage.getItem("host");
    const [modelType, setModelType] = useState('');
    const [selectedProduct, setSelectedProduct] = useState(null);
    const [productList, setProductList] = useState([]);
    const [allProd, setAllProd] = useState([]);
    const [allProducts, setAllProducts] = useState([]);
    const [loginuser, setLoginuser] = useState('');
    const url = "http://" + host;

    useEffect(() => {
        ProductListing();
        GetLoginDetails()
    }, [])
    async function GetLoginDetails() {
        const user = await axios.get(url + "/identities");
        setLoginuser(user.data);

    }

    const sendUpdateDetails = (product) => {
        setModelType('update');
        setSelectedProduct(product);
    }

    async function ProductListing() {
        setAllProducts([]);
        setAllProd([]);
        try {
            let Products_fetched = await axios.get(url + "/products")

            Products_fetched.data.forEach(async product => {
                let result = product.state.data;
                setAllProd(oldDta => [...oldDta, { serialNumber: product, name: result.Name, ProductId: result.ProductID, Description: result.Description, Quantity: result.Quantity, Owner: result.owner }])
                setAllProducts(oldDta => [...oldDta, { serialNumber: product, name: result.Name, ProductId: result.ProductID, Description: result.Description, Quantity: result.Quantity, Owner: result.owner }])
                setProductList(Products_fetched.data);
            })
        }
        catch (error) {
            throw error;
        }
    }
    function filterByParam(filterValue, name = 'name') {
        let products;
        if (name === 'status') {
            if (filterValue.toLowerCase() == 'false') {
                filterValue = false;
                allProd.forEach(prod => {
                    if (prod.status == filterValue) {
                        products.push(prod);
                    }
                })
                setAllProd(products);
            }
            else if (filterValue.toLowerCase() == 'true') {
                filterValue = true;
                allProd.forEach(prod => {
                    if (prod.status == filterValue) {
                        products.push(prod);
                    }
                })
                setAllProd(products);
            }
            else if (filterValue.toLowerCase() == 'all') {
                setAllProd(allProducts)
            }
        }
        filterValue === 'All' ? setAllProd(allProducts) : setAllProd(allProducts.filter(product => product[name] === filterValue));
    }

    function doSearch(filterValue) {
        filterValue === 'All' ? setAllProd(allProducts) : setAllProd(allProducts.filter(product => product['name'].toLowerCase().includes(filterValue.toLowerCase())));
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
                <section className="page-title mt-3 mx-4">
                    <div className="container-fluid">
                        <div className="row ">
                            <h2 className="title mb-1 p-0 pb-3 helvetica-mediumv fw-bold main-color">All Products</h2>
                        </div>
                    </div>
                </section>
                <section className="participants mt-3 mx-4">
                    <div className="container-fluid">
                        <div className="row">
                            <div className="table-block bg-white p-4 mb-5 rounded">
                                <div className="block-header d-flex justify-content-between">
                                    <div className="search-block d-flex">
                                        <button className="btn btn-primary me-3" type="button" data-bs-toggle="modal" data-bs-target="#add-product" onClick={() => setModelType('add')}>
                                            Add Products
                                        </button>
                                        <form className="d-flex">
                                            <input className="form-control me-2" type="search" placeholder="Search" aria-label="Search" onChange={(e) => doSearch(e.target.value, 'products')} />
                                        </form>
                                    </div>
                                </div>
                                <div className="block-filters mt-3 d-flex justify-content-between">
                                    <div className="filters-list d-flex">
                                        <div className="filter me-3">
                                            <h6 className="sub-color">Product Name</h6>
                                            <select className="form-select main-color helvetica-light" aria-label="Product Name option" onChange={(e) => filterByParam(e.target.value, 'name')}>
                                                <option defaultValue>All</option>
                                                {allProducts.map((product, index) => {
                                                    return (
                                                        <option key={index * 19} value={product.name}>{product.name}</option>
                                                    )
                                                })}
                                            </select>
                                        </div>
                                        <div className="filter me-3">
                                            <h6 className="sub-color">Present Location</h6>
                                            <select className="form-select main-color helvetica-light" aria-label="Present Location option" onChange={(e) => filterByParam(e.target.value, 'location')}>
                                                <option defaultValue>All</option>
                                                <option value="Distributor">Supplier</option>
                                                <option value="Manufacturer">Manufacturer</option>
                                                <option value="Wholesaler">Wholesaler</option>       
                                            </select>
                                        </div>
                                        <div className="filter me-3">
                                            <h6 className="sub-color">Transfer Status</h6>
                                            <select className="form-select main-color helvetica-light" aria-label="Product Status option" onChange={(e) => filterByParam(e.target.value, 'TransferStatus')}>
                                                <option defaultValue>All</option>
                                                <option value="Received">Received</option>
                                                <option value="Manufactured">Manufactured</option>
                                                <option value="Manufactured">Transfered</option>
                                            </select>
                                        </div>
                                    </div>
                                </div>
                                <div className="table-container mt-3">
                                    <div className="table-responsive">
                                        <table className="table align-middle">
                                            <thead>
                                                <tr className="text-capitalize">
                                                    <th scope="col">ProductID</th>
                                                    <th scope="col" className="sorting desc">ProductName</th>
                                                    <th scope="col">Description</th>
                                                    <th scope="col">Quantity</th>
                                                    <th scope="col">Owner</th>
                                                </tr>
                                            </thead>
                                            <tbody>
                                                {allProd.map((product, index) => {
                                                    return (
                                                        <tr key={index}>
                                                            <td>{product.ProductId}</td>
                                                            <td>{product.name}</td>
                                                            <td>{product.Description}</td>
                                                            <td>{product.Quantity}</td>
                                                            <td>{product.Owner}</td>
                                                            <td><button type="button" className="btn btn-outline-primary" data-bs-toggle="modal" data-bs-target="#add-product" onClick={() => sendUpdateDetails(product)}>Update</button></td>
                                                        </tr>
                                                    );
                                                })}
                                            </tbody>
                                        </table>
                                        <div className=" table-footer d-flex  justify-content-between align-items-baseline">
                                            <div className="total_items text-muted">Total Items: {productList.length}</div>
                                            {productList.length > 10 && <div className="Tables_paginate d-flex me-2">
                                                <div className="table-count me-3">
                                                    <label for="inputcount" className="form-label me-2 text-muted">Items per page:</label>
                                                    <select id="inputcount" className="form-select d-inline-block w-auto">
                                                        <option value="10" defaultValue>10</option>
                                                        <option value="25">25</option>
                                                        <option value="50">50</option>
                                                        <option value="100">100</option>
                                                    </select>
                                                </div>
                                                <div className="paginate-block ">
                                                    <nav aria-label="Page navigation">
                                                        <ul className="pagination">
                                                            <li className="page-item disabled"><a className="page-link" href="#">Previous</a></li>
                                                            <li className="page-item active"><a className="page-link" href="#">1</a></li>
                                                            <li className="page-item"><a className="page-link" href="#">2</a></li>
                                                            <li className="page-item"><a className="page-link" href="#">Next</a></li>
                                                        </ul>
                                                    </nav>
                                                </div>
                                            </div>}
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </section>
                <div className="model-section">
                    <AddProduct id="add-product" title={modelType === 'add' ? 'Add Product' : 'Update Product'} selectedProduct={selectedProduct} host={host} />
                </div>

            </main>
        </div>
    )
}