import profileImg from "../assets/images/profile.jpeg"
import { locationIcon, mailIcon, starIcon, phoneIcon } from "../assets/icons"
import "../styles/TraderProfile.css"
import ProductCard from "../components/ProductCard"
import { products } from "@/constants"

import gsap from 'gsap';
import { useGSAP } from '@gsap/react';
import { useQuery } from "@tanstack/react-query"
import axios from 'axios';
import { useParams } from "react-router-dom"

const TraderProfile = () => {
  const { userId } = useParams()

  const { status, error, data } = useQuery({
    queryKey: ["traderAccount"],
    queryFn: async () => {
      return axios
        .get(`/api/accounts/${userId}`)
        .then(res => {
          console.log(res.data)
          if (res.data == "") {
            window.location.pathname = "/error"
          }
          return res.data
        })
        .catch(err => console.log(err))
    }
  })



  const infoItems = [
    { icon: mailIcon, text: data?.userEmail },
    { icon: locationIcon, text: data?.userLocation },
    { icon: phoneIcon, text: data?.userContact },
    { icon: starIcon, text: "fali/5" },
  ]


  useGSAP(() => {
    gsap.from(".product-card", {
      opacity: 0,
      ease: "power1.inOut",
      y: 25,
      stagger: .06,
      duration: .5
    })
  }, [products])


  return (
    <section className="padding-x pt-30 sm:pt-45 pb-20">
      <section className="max-container">

        <section id="info-container" className="mb-19 sm:mb-34">
          <img
            id="profile-image"
            src={profileImg}
            alt="profile"
            className="sm:ml-[-10px] ml-[-4px] rounded-full h-[152px] w-[152px] sm:h-[206px] sm:w-[206px] object-cover"
          />
          <div id="name">
            <h1 className="text-white text-2xl sm:text-[32px] font-medium mb-1">{data?.userFirstName} {data?.userLastName}</h1>
            <p className="font-inter text-desc capitalize">{data?.accountRole}</p>
          </div>

          <div id="desc" className="flex gap-4 flex-col">
            {infoItems.map((item) => (
              <div key={item.text} className="text-white font-inter flex items-start gap-3">
                <img src={item.icon} alt="list item icon" />
                {item.text}
              </div>
            ))}
          </div>


        </section>


        {/* <section className="flex gap-11 flex-wrap"> */}
        {data?.accountRole == "trader" &&
          <>
            <h1 className="text-white font-inter font-medium text-xl sm:text-2xl mb-[54px]">{data?.userFirstName} {data?.userLastName} is renting</h1>
            <section className="grid min-[1250px]:grid-cols-4 min-[1000px]:grid-cols-3 min-[700px]:grid-cols-2 grid-cols-1 max-sm:justify-items-center gap-11">
              {products.map((product, index) => (
                <div key={index} className="product-card">
                  <ProductCard
                    category={product.category}
                    img={product.img}
                    productName={product.productName}
                    owner={product.owner}
                    desc={product.desc}
                    price={product.price}
                    productUrl={product.productUrl}
                  ></ProductCard>
                </div>
              ))}
            </section>
          </>
        }

      </section>
    </section>
  );
}


export default TraderProfile;
