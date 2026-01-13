import { MapContainer, TileLayer, Marker, Popup } from "react-leaflet";
import "leaflet/dist/leaflet.css";

const position: [number, number] = [45.8150, 15.9819]; // Zagreb 

export default function Map({ products }: { products: any[] }) {
  return (
    <MapContainer
      center={position}
      zoom={8}
      style={{ height: "520px", width: "100%" }}

    >
      <TileLayer
        attribution='&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
        url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
      />

      {products ?.filter((p) => p?.latitude != null && p?.longitude != null).map((p) => (
          <Marker
            key={p.advertisementId}
            position={[p.latitude, p.longitude]}
          >
            <Popup>
              <strong>{p.itemName ?? "Oglas"}</strong>
              <br />
              {p.advertisementLocationTakeover ?? ""}
            </Popup>
          </Marker>
        ))}
    </MapContainer>
  );
}
