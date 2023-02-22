import { useEffect, useState } from "react";
import axios from 'axios';
import $ from "jquery";



export default function AddProduct(props) {
    const host = localStorage.getItem("host");
    const { title, selectedProduct } = props;
    const [ProductID, setProductID] = useState('');
    const [Name, setName] = useState('');
    const [Description, setDescription] = useState('');
    const [Quantity, setQuantity] = useState('');
    const [result, setResult] = useState('');
    let url = 'http://';

    

    async function createProduct(ProductID, Name, Description, Quantity) {
        const products=await axios.get(url +host+ "/products");
        try {
            products.data.forEach(async (product, index) => {
                let result = product.state.data;
                console.log(result.ProductID);
            if(ProductID!=result.ProductId){
                axios.post(url + host + '/create-product', new URLSearchParams({
                    "ProductID": ProductID,
                    "Name": Name,
                    "Description": Description,
                    "Quantity": Quantity
                }))
            }
        })
        }
        catch (Error) {
            throw Error
        }

        // window.location.reload(false)

    }

    async function UpdateProduct(selectedProduct) {


        try {
            if(selectedProduct){
                // await axios.put(url + host + '/update-product', new URLSearchParams({
                //     "ProductID": ProductID,
                //     "Name": Name,
                //     "Description": Description,
                //     "Quantity": Quantity
                // }))
                const data = {"ProductID": ProductID,
                    "Name": Name,
                    "Description": Description,
                    "Quantity": Quantity};
                await fetch(url, {
                    method: 'PUT', // *GET, POST, PUT, DELETE, etc.
                    mode: 'cors', // no-cors, *cors, same-origin
                    cache: 'no-cache', // *default, no-cache, reload, force-cache, only-if-cached
                    credentials: 'same-origin', // include, *same-origin, omit
                    headers: {
                    //   'Content-Type': 'application/json'
                      'Content-Type': 'application/x-www-form-urlencoded',
                    },
                    redirect: 'follow', // manual, *follow, error
                    referrerPolicy: 'no-referrer', // no-referrer, *no-referrer-when-downgrade, origin, origin-when-cross-origin, same-origin, strict-origin, strict-origin-when-cross-origin, unsafe-url
                    body: JSON.stringify(data) // body data type must match "Content-Type" header
                  });
            }
        }
        catch (Error) {
            throw Error;
        }
        // window.location.reload(false)
    }

    useEffect(() => {

    }, [props, selectedProduct])

    const saveProduct = (event) => {
        event.preventDefault();

        if (title === 'Add Product') {
            createProduct(ProductID, Name, Description, Quantity);
        }
        else {
            UpdateProduct(selectedProduct);
        }
    }

    const resetFields = () => {
        $("#").children("input").value = "";
    }



    return (
        <div >
            <div className="modal-dialog">
                <div className="modal-content p-3">
                    <div className="modal-header">
                        <h4 className="modal-title helvetica-medium main-color fw-bold" id="modal-title">{title || 'Add Product'}</h4>
                        <button type="button" className="btn-close" data-bs-dismiss="modal" aria-label="Close" onClick={(e) => { resetFields(e) }}></button>
                    </div>
                    <div className="modal-body">
                        <form className=" g-3 helvetica-light sub-color">
                            <div className="row mb-2">
                                <div className="col-12 col-md-6">
                                    <label htmlFor="product-id" className="form-label">Product ID</label>
                                    <input type="text" className="form-control" id="Product-id" value={ProductID} onChange={(e) => setProductID(e.target.value)} />
                                </div>
                                <div className="col-12 col-md-6 ">
                                    <label htmlFor="product-name" className="form-label">Product Name</label>
                                    <input type="text" className="form-control" id="product-name" value={Name} onChange={(e) => setName(e.target.value)} />
                                </div>
                            </div>
                            <div className="row mb-2">
                                <div className="col-12 col-md-6">
                                    <label htmlFor="description" className="form-label">Description</label>
                                    <input type="text" className="form-control" id="description" value={Description} onChange={(e) => setDescription(e.target.value)} />
                                </div>
                            </div>
                            <div className="row mb-2">
                                <div className="col-12 col-md-6">
                                    <label htmlFor="quantity" className="form-label">Quantity</label>
                                    <input type="text" className="form-control" id="quantity" value={Quantity} onChange={(e) => setQuantity(e.target.value)} />
                                </div>
                            </div>
                            <div className="row mb-2">
                                <div className="col-12 text-end">

                                    <button className="btn btn-primary" data-bs-dismiss="modal" type="submit" onClick={(e) => saveProduct(e)}>Save</button>
                                </div>
                            </div>
                        </form>
                    </div>
                </div>
                <div className="modal-footer d-none">
                        <button type="button" className="btn btn-secondary" data-bs-dismiss="modal" onClick={(e) => { resetFields(e) }}>Close</button>
                        <button type="button" className="btn btn-primary">Understood</button>
                    </div>
            </div>

        </div>
    )
}