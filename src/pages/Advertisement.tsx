import { locationIcon } from "@/assets/icons";
import Button from "@/components/Button";
import bikeImg from "@/assets/images/bike.jpeg"
import { useQuery, useMutation } from "@tanstack/react-query";
import axios from "axios";
import { useParams } from "react-router-dom";
import { useState, useEffect } from "react";
import useAuthContext from "@/hooks/useAuthContext";
import { editIcon, deleteIcon } from "@/assets/icons";

const Advertisement = () => {

  const { advertisementId } = useParams()
  const [currentUserIsOwner, setCurrentUserIsOwner] = useState<boolean>(false)

  const { user } = useAuthContext()

  const rentFun = useMutation({
    mutationFn: async () => {
      if (!user?.accountId) {
        window.location.pathname = "/auth";
      }
      return axios.post(`/api/reservations/create`, {
        buyerId: user.accountId,
        advertisementId: advertisementId
      });
    },
    onSuccess: () => {
      window.location.pathname = "/";
    }
  });


  const { data } = useQuery({
    queryKey: ["advertisementData"],
    queryFn: async () => {
      return axios
        .get(`/api/advertisements/${advertisementId}`)
        .then(res => {
          if (res.data == "") {
            window.location.pathname = "/error"
          }
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
    setCurrentUserIsOwner(data?.trader.accountId === user?.accountId && user?.accountId !== undefined)
  }, [data])

  return (
    <section className="padding-x padding-t padding-b">
      <section className="max-container">
        <div className="flex gap-8 lg:gap-20 max-lg:flex-col">
          <img src={bikeImg} alt="product image" className="lg:w-2/5 object-cover object-center rounded-[8px] w-full max-h-[300px] lg:max-h-[400px]" />
          <div className="flex flex-col items-start gap-10 lg:gap-12 flex-1">
            <div className="flex items-start gap-1 flex-col w-full">
              <div className="flex gap-2 items-center mb-3">
                {currentUserIsOwner &&
                  <>
                    <a
                      href={`/`}
                      className="rounded-[8px] hover:shadow-[0_0_15px_#E51739] transition-shadow duration-300 pl-2 pr-2.5 py-1.25 bg-primary text-black flex items-center gap-1 max-sm:text-[14px]"
                    >
                      <img src={editIcon} alt="edit icon" className="w-4 h-4" />
                      Edit
                    </a>
                    <a
                      href={`/`}
                      className="rounded-[8px] hover:shadow-[0_0_15px_#FF264A] transition-shadow duration-300 pl-2 pr-2.5 py-1.25 bg-[#FF1F44] text-black flex items-center gap-1 max-sm:text-[14px]"
                    >
                      <img src={deleteIcon} alt="edit icon" className="w-4 h-4" />
                      Delete
                    </a>
                  </>
                }

              </div>
              <p className="text-primary bg-[#102B19] rounded-[8px] px-2.5 py-1.25 text-[13px] font-inter">{data?.itemType.itemtypeName}</p>
              <h1 className="font-inter text-[24px] sm:text-[32px] font-medium text-white mr-8 mb-1">{data?.itemName}</h1>
              <p className="font-inter text-white text-[14px]">by <a className="font-medium" href={`/profile/${data?.trader.accountId}`}>{data?.trader.userFirstName} {data?.trader.userLastName}</a></p>
            </div>
            <p className="font-inter text-white text-[16px] max-w-[700px]"> {data?.itemDescription}</p>

            <div className="text-white font-inter flex flex-col gap-5">
              <div className="flex items-start max-w-[700px] gap-1">
                <img src={locationIcon} alt="location icon" />
                <p>Preuzimanje — {data?.advertisementLocationTakeover}</p>
              </div>
              <div className="flex items-start max-w-[700px] gap-1">
                <img src={locationIcon} alt="location icon" />
                <p>Povrat — {data?.advertisementLocationReturn}</p>
              </div>
            </div>

            <div className="flex font-inter items-center gap-11">
              <div className="flex flex-col items-start flex-1 justify-start text-white min-w-[100px]">
                <h1 className="font-medium text-[32px]">{data?.advertisementPrice}€</h1>
                <p className="text-desc text-sm">Kaucija {data?.advertisementDeposit}€</p>
              </div>
              {!currentUserIsOwner &&
                <Button text="Rent" icon={true} long={false} onClick={() => rentFun.mutate()} />
              }
            </div>

          </div>
        </div>

      </section>
    </section>
  );
}

export default Advertisement;
