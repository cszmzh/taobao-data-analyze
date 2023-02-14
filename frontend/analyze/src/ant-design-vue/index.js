import {
    Form, Input, Button, Layout, Menu, Row, Col,
    Dropdown, Avatar, Tabs, Table, Alert, Card, List, Cascader,
    Pagination, Divider, Popconfirm, Modal, Select, Skeleton, Statistic,
    Descriptions, Badge, Comment, Tooltip, Tag, Tree
} from 'ant-design-vue';
import 'ant-design-vue/dist/antd.css';

const components = [
    Form,
    Input,
    Button,
    Layout,
    Menu,
    Row,
    Col,
    Dropdown,
    Avatar,
    Tabs,
    Table,
    Alert,
    Card,
    List,
    Pagination,
    Divider,
    Popconfirm,
    Modal,
    Select,
    Skeleton,
    Statistic,
    Descriptions,
    Badge,
    Comment,
    Tooltip,
    Cascader,
    Tag,
    Tree
]
export function setupAntd(app) {
    components.forEach(component => {
        app.use(component)
    })
}