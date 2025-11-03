import { locationIcon } from "@/assets/icons";
import Button from "@/components/Button";
import bikeImg from "@/assets/images/bike.jpeg"
import { useQuery } from "@tanstack/react-query";
import axios from "axios";
import { useParams } from "react-router-dom";

const Advertisement = () => {

  const { advertisementId } = useParams()

  const { data } = useQuery({
    queryKey: ["advertisementData"],
    queryFn: async () => {
      return axios
        .get(`/api/advertisements/${advertisementId}`)
        .then(res => {
          console.log(res.data)
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


  return (
    <section className="padding-x padding-t">
      <section className="max-container">

        <div className="flex gap-8 lg:gap-20 max-lg:flex-col">
          <img src={bikeImg} alt="product image" className="max-h-[300px] lg:max-h-[400px]  lg:w-2/5 object-cover object-center rounded-[8px]" />
          <div className="flex flex-col items-start gap-10 lg:gap-12">
            <div className="flex items-start gap-1 flex-col">
              <p className="text-primary bg-[#102B19] rounded-[8px] px-2.5 py-1.75 text-[14px] font-inter">{data?.itemType.itemtypeName}</p>
              <h1 className="font-inter text-[24px] sm:text-[32px] font-medium text-white">{data?.itemName}</h1>
              <p className="font-inter text-white text-[14px]">by <a className="font-medium" href={`/profile/${data?.trader.accountId}`}>{data?.trader.userFirstName} {data?.trader.userLastName}</a></p>
            </div>
            <p className="font-inter text-white text-[16px] max-w-[700px]"> {data?.itemDescription}</p>

            <div className="text-white font-inter flex flex-col gap-5">
              <div className="flex items-start max-w-[700px] gap-1">
                <img src={locationIcon} alt="location icon" />
                <p>{data?.advertisementLocationTakeover}</p>
              </div>
              <div className="flex items-start max-w-[700px] gap-1">
                <img src={locationIcon} alt="location icon" />
                <p>{data?.advertisementLocationReturn}</p>
              </div>
            </div>

            <div className="flex font-inter items-center gap-11">
              <div className="flex flex-col items-start flex-1 justify-start text-white min-w-[100px]">
                <h1 className="font-medium text-[32px]">{data?.advertisementPrice}€</h1>
                <p className="text-desc text-sm">Kaucija {data?.advertisementDeposit}€</p>
              </div>
              <Button text="Rent" icon={true} long={false} onClick={() => { }} />
            </div>

          </div>
        </div>

      </section>
    </section>
  );
}

export default Advertisement;
