import profileImg from "../assets/images/profile.jpeg"
import { locationIcon, mailIcon, starIcon, phoneIcon } from "../assets/icons"
import "../styles/Profile.css"

import AdvertisementsGrid from "@/components/AdvertisementsGrid"
import { useQuery } from "@tanstack/react-query"
import axios from 'axios';
import { useParams } from "react-router-dom"
import { useState, useEffect } from "react"
import { useMutation } from "@tanstack/react-query"
import ReservationHistory from "@/components/ReservationHistory"

const Profile = () => {
  const { userId } = useParams()

  const [products, setProducts] = useState([])

  const { data: accountData } = useQuery({
    queryKey: ["account"],
    queryFn: async () => {
      return axios
        .get(`/api/accounts/${userId}`)
        .then(res => {
          if (res.data == "") {
            window.location.pathname = "/error"
          }
          return res.data
        })
        .catch(err => console.log(err))
    }
  })

  const { data: reservationsData } = useQuery({
    queryKey: ["reservations"],
    enabled: accountData !== undefined,
    queryFn: async () => {
      return axios
        .get(`/api/reservations/${accountData?.accountRole}/${userId}`)
        .then(res => {
          return res.data
        })
        .catch(err => {
          console.log(err)
          if (err.response.status === 400) {
            window.location.pathname = "/error"
          }
        })
    }
  })


  useEffect(() => {
    mutate()
  }, [])

  const { mutate } = useMutation({
    mutationFn: async () => {
      return axios
        .get(`/api/advertisements/account/${userId}`)
        .then(res => {
          return res.data
        })
        .catch(err => console.log(err))
    },
    onSuccess: queryResult => {
      setProducts(queryResult)
    }

  })

  const infoItems = [
    { icon: mailIcon, text: accountData?.userEmail },
    { icon: locationIcon, text: accountData?.userLocation },
    { icon: phoneIcon, text: accountData?.userContact },
  ]
  if (accountData?.accountRating) infoItems.push({ icon: starIcon, text: `${accountData?.accountRating}/5` })


  return (
    <section className="padding-x padding-t pb-20">
      <section className="max-container">

        <section id="info-container" className="mb-19 sm:mb-34 max-sm:!gap-x-[0px]">
          <img
            id="profile-image"
            src={profileImg}
            alt="profile"
            className="sm:ml-[-10px] ml-[-4px] rounded-full h-[110px] w-[110px] sm:h-[206px] sm:w-[206px] object-cover"
          />
          <div id="name" className="max-sm:ml-[-20px] ">
            <h1 className="text-white text-2xl sm:text-[32px] font-medium mb-1">{accountData?.userFirstName} {accountData?.userLastName}</h1>
            <p className="font-inter text-desc capitalize">{accountData?.accountRole}</p>
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

        {accountData?.accountRole == "trader" &&
          <>
            <h1 className="text-white font-inter font-medium text-xl sm:text-2xl mb-[54px]">{accountData?.userFirstName} {accountData?.userLastName} {products.length === 0 ? "is not renting any products" : "is renting"}</h1>
            <AdvertisementsGrid products={products} />
          </>
        }

        <ReservationHistory reservationsData={reservationsData} accountRole={accountData?.accountRole} />

      </section>
    </section >
  );
}


export default Profile;
