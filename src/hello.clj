(ns hello
  (:require
   [aleph.http :as http]
   [byte-streams :as byte-streams]
   [clojure.pprint :refer [pprint]]
   [clojure.spec.alpha :as s]
   [manifold.deferred :as deferred]))

;; -----------------------------------------------------------------------------
;; Specs

(s/def ::request-method #{:delete :get :head :patch :post :put :trace})
(s/def ::url string?)
(s/def ::request (s/keys :req-un [::request-method ::url]))

;; -----------------------------------------------------------------------------
;; Fetch

(s/fdef fetch-response
  :args (s/cat :request ::request))

(defn- fetch-response
  [request]
  (deferred/chain
    (http/request request)
    #(update % :body byte-streams/to-string)))

;; -----------------------------------------------------------------------------
;; Main

(def ^:private request
  {:request-method :get
   :url "https://www.jamesconroyfinn.com/"})

(defn -main
  []
  (pprint @(fetch-response request)))
